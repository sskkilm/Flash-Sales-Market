package com.example.product.common.dto;

import com.example.product.domain.NormalProduct;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record NormalProductDetails(
        Long productId,
        String name,
        BigDecimal price,
        String type,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static NormalProductDetails from(NormalProduct product) {
        return new NormalProductDetails(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getType().name(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
