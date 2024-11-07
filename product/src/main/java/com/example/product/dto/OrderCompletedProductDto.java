package com.example.product.dto;

public record OrderCompletedProductDto(
        Long productId,
        int quantity
) {
}
