package com.example.product;

import com.example.common.domain.Money;
import com.example.product.application.ProductRepository;
import com.example.product.domain.Product;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestProductDataInit {

    private final ProductRepository productRepository;

    @PostConstruct
    public void init() {
        Product product1 = Product.builder()
                .name("상품1")
                .price(Money.of("10000"))
                .stockQuantity(10)
                .build();
        Product product2 = Product.builder()
                .name("상품2")
                .price(Money.of("20000"))
                .stockQuantity(20)
                .build();

        productRepository.save(product1);
        productRepository.save(product2);
    }
}
