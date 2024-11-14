package com.example.product.application;

import com.example.product.domain.HoldStock;
import com.example.product.domain.LimitedProduct;
import com.example.product.domain.Product;
import com.example.product.dto.StockHoldInfo;
import com.example.product.dto.StockHoldRequest;
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
    HoldStockService holdStockService;

    @Autowired
    HoldStockRepository holdStockRepository;

    @BeforeEach
    public void clear() {
        productRepository.deleteAll();
        holdStockRepository.deleteAll();
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

        List<StockHoldInfo> stockHoldInfos = List.of(
                new StockHoldInfo(product.getId(), 1)
        );

        //when
        for (int i = 0; i < threadCount; i++) {
            final int finalI = i + 1;
            executorService.submit(() -> {
                try {
                    StockHoldRequest stockHoldRequest = new StockHoldRequest(
                            (long) finalI, stockHoldInfos
                    );
                    productService.holdStock(stockHoldRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        assertEquals(10, holdStockService.getHoldStockQuantityInProduct(product.getId()));
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
        for (int i = 0; i < threadCount; i++) {
            long orderId = i + 1;
            HoldStock holdStock = HoldStock.create(orderId, product.getId(), 1);
            holdStockRepository.save(holdStock);
        }

        //when
        for (int i = 0; i < threadCount; i++) {
            final long orderId = i + 1;
            executorService.submit(() -> {
                try {
                    productService.applyHoldStock(orderId);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        int stockQuantity = productRepository.findById(product.getId()).getStockQuantity();
        assertEquals(0, stockQuantity);
        int holdStockQuantityInProduct = holdStockService.getHoldStockQuantityInProduct(product.getId());
        assertEquals(0, holdStockQuantityInProduct);
    }
}
