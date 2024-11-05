package com.example.product.dto;

import com.example.product.domain.LimitedProduct;
import com.example.product.domain.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LimitedProductDetails(
        Long productId,
        String name,
        BigDecimal price,
        String type,
        LocalDateTime openTime,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static LimitedProductDetails from(Product product) {
        LimitedProduct limitedProduct = (LimitedProduct) product;
        return new LimitedProductDetails(
                limitedProduct.getId(),
                limitedProduct.getName(),
                limitedProduct.getPrice(),
                limitedProduct.getType().name(),
                limitedProduct.getOpenTime(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}
