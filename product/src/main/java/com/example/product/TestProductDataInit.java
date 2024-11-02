package com.example.product;

import com.example.product.application.ProductRepository;
import com.example.product.domain.LimitedProduct;
import com.example.product.domain.NormalProduct;
import com.example.product.domain.Product;
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
        NormalProduct normalProduct = NormalProduct.builder()
                .name("normal product")
                .price(new BigDecimal("10000"))
                .stockQuantity(10)
                .build();
        LimitedProduct limitedProduct = LimitedProduct.builder()
                .name("limited product")
                .price(new BigDecimal("20000"))
                .stockQuantity(20)
                .openTime(LocalDateTime.MAX)
                .build();

        productRepository.save(normalProduct);
        productRepository.save(limitedProduct);
    }
}
