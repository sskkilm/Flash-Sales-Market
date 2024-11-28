package com.example.product.common.dto;

import com.example.product.domain.Product;

import java.math.BigDecimal;

public record ProductDto(
        Long productId,
        String name,
        BigDecimal price
) {

    public static ProductDto from(Product product) {
        return new ProductDto(
                product.getId(),
                product.getName(),
                product.getPrice()
        );
    }
}
