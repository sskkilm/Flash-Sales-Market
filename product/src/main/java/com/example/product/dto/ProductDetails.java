package com.example.product.dto;

import com.example.product.domain.Product;

import java.time.LocalDateTime;

public record ProductDetails(
        Long id,
        String name,
        String price,
        int stockQuantity,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductDetails from(Product product) {
        return new ProductDetails(
                product.getId(),
                product.getName(),
                product.getPrice().toString(),
                product.getStockQuantity(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
