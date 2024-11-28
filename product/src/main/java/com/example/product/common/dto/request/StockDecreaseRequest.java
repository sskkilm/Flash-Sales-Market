package com.example.product.common.dto.request;

public record StockDecreaseRequest(
        Long productId,
        int quantity
) {
}
