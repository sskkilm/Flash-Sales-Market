package com.example.product;

import com.example.product.application.HoldingStockService;
import com.example.product.application.ProductRepository;
import com.example.product.application.ProductService;
import com.example.product.domain.LimitedProduct;
import com.example.product.domain.Product;
import com.example.product.dto.ProductOrderInfo;
import com.example.product.dto.ProductOrderRequest;
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
public class HoldingStockConcurrencyTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    HoldingStockService holdingStockService;

    @BeforeEach
    public void clear() {

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

        List<ProductOrderInfo> productOrderInfos = List.of(
                new ProductOrderInfo(product.getId(), 1)
        );

        //when
        for (int i = 0; i < threadCount; i++) {
            final int finalI = i + 1;
            executorService.submit(() -> {
                try {
                    ProductOrderRequest productOrderRequest = new ProductOrderRequest(
                            (long) finalI, productOrderInfos
                    );
                    productService.order(productOrderRequest);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        assertEquals(10, holdingStockService.getHoldingStockQuantityInProduct(product.getId()));
    }
}
