package com.example.product.application;

import com.example.product.application.port.ProductRepository;
import com.example.product.application.port.StockPreoccupationRepository;
import com.example.product.domain.LimitedProduct;
import com.example.product.domain.PreoccupiedStock;
import com.example.product.domain.Product;
import com.example.product.dto.StockPreoccupationInfo;
import com.example.product.dto.StockPreoccupationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
public class StockConcurrencyTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    StockPreoccupationService stockPreoccupationService;

    @Autowired
    StockPreoccupationRepository stockPreoccupationRepository;

    @BeforeEach
    public void clear() {
        productRepository.deleteAll();
        stockPreoccupationRepository.deleteAll();
    }

    @Test
    void 동시에_재고를_선점한다() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        LocalDateTime now = LocalDateTime.now();
        Product product = productRepository.save(
                LimitedProduct.builder()
                        .name("limited product")
                        .price(new BigDecimal("20000"))
                        .stockQuantity(10)
                        .openTime(now.minusMinutes(10))
                        .build()
        );

        List<StockPreoccupationInfo> stockPreoccupationInfos = List.of(
                new StockPreoccupationInfo(product.getId(), 1)
        );

        //when
        for (int i = 0; i < threadCount; i++) {
            final int finalI = i + 1;
            executorService.submit(() -> {
                try {
                    StockPreoccupationRequest stockPreoccupationRequest = new StockPreoccupationRequest(
                            (long) finalI, stockPreoccupationInfos
                    );
                    productService.preoccupyStock(stockPreoccupationRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        assertEquals(10, stockPreoccupationService.getPreoccupiedStockQuantityInProduct(product.getId()));
    }

    @Test
    void 동시에_재고를_차감한다() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        LocalDateTime now = LocalDateTime.now();
        Product product = productRepository.save(
                LimitedProduct.builder()
                        .name("limited product")
                        .price(new BigDecimal("20000"))
                        .stockQuantity(100)
                        .openTime(now.minusMinutes(10))
                        .build()
        );
        for (int i = 0; i < 100; i++) {
            long orderId = i + 1;
            PreoccupiedStock preoccupiedStock = PreoccupiedStock.create(orderId, product.getId(), 1);
            stockPreoccupationRepository.save(preoccupiedStock);
        }

        //when
        for (int i = 0; i < threadCount; i++) {
            final long orderId = i + 1;
            executorService.submit(() -> {
                try {
                    productService.applyPreoccupiedStock(orderId, List.of(product.getId()));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        int stockQuantity = productRepository.findById(product.getId()).getStockQuantity();
        assertEquals(0, stockQuantity);
        int holdStockQuantityInProduct = stockPreoccupationService.getPreoccupiedStockQuantityInProduct(product.getId());
        assertEquals(0, holdStockQuantityInProduct);
    }
}
