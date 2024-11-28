package com.example.order.common.dto.request;

public record StockDecreaseRequest(
        Long productId,
        int quantity
) {
}
