package com.example.product.application;

import com.example.product.application.port.ProductRepository;
import com.example.product.domain.EventProduct;
import com.example.product.domain.Product;
import com.example.product.common.dto.request.StockDecreaseRequest;
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

    @BeforeEach
    public void clear() {
        productRepository.deleteAll();
    }

    @Test
    void 동시에_재고를_차감한다() throws InterruptedException {
        //given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        LocalDateTime now = LocalDateTime.now();
        Product product = productRepository.save(
                EventProduct.builder()
                        .name("Event Product")
                        .price(new BigDecimal("20000"))
                        .stockQuantity(100)
                        .openTime(now.minusMinutes(10))
                        .build()
        );
        StockDecreaseRequest request = new StockDecreaseRequest(
                product.getId(), 1
        );

        //when
        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    productService.decreaseStock(List.of(request));
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();

        //then
        int stockQuantity = productRepository.findById(product.getId()).getStockQuantity();
        assertEquals(0, stockQuantity);
    }

}
