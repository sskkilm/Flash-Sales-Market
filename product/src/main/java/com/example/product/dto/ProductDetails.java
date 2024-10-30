package com.example.product.dto;

import com.example.product.domain.Product;

import java.math.BigDecimal;

public record ProductDetails(
        Long productId,
        String name,
        BigDecimal price,
        int stockQuantity
) {
    public static ProductDetails from(Product product) {
        return new ProductDetails(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }
}
