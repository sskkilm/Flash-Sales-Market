package com.example.product;

import com.example.product.application.ProductRepository;
import com.example.product.domain.LimitedProduct;
import com.example.product.domain.NormalProduct;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestProductDataInit {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        LocalDateTime now = LocalDateTime.now();

        NormalProduct product1 = NormalProduct.builder()
                .name("normal product1")
                .price(new BigDecimal("10000"))
                .stockQuantity(100)
                .build();
        LimitedProduct product2 = LimitedProduct.builder()
                .name("limited product2")
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .openTime(now.minusMinutes(10))
                .build();
        LimitedProduct product3 = LimitedProduct.builder()
                .name("limited product3")
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .openTime(now.plusMinutes(10))
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
    }
}
