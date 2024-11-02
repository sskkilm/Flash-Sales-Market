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
        NormalProduct product1 = NormalProduct.builder()
                .name("normal product")
                .price(new BigDecimal("10000"))
                .stockQuantity(100)
                .build();
        LimitedProduct product2 = LimitedProduct.builder()
                .name("limited product")
                .price(new BigDecimal("20000"))
                .stockQuantity(10)
                .openTime(LocalDateTime.of(
                        2024, 11, 2, 12, 0, 0
                ))
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
    }
}
