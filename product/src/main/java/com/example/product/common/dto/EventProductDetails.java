package com.example.product.common.dto;

import com.example.product.domain.EventProduct;
import com.example.product.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record EventProductDetails(
        Long productId,
        String name,
        BigDecimal price,
        String type,
        LocalDateTime openTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EventProductDetails from(Product product) {
        EventProduct eventProduct = (EventProduct) product;
        return new EventProductDetails(
                eventProduct.getId(),
                eventProduct.getName(),
                eventProduct.getPrice(),
                eventProduct.getType().name(),
                eventProduct.getOpenTime(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
