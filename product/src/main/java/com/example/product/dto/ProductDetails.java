package com.example.product.dto;

import com.example.product.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductDetails(
        Long productId,
        String name,
        BigDecimal price,
        String type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProductDetails from(Product product) {
        return new ProductDetails(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getType().name(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
