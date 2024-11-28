package com.example.order.common.dto.request;

public record StockIncreaseRequest(
        Long productId,
        int quantity
) {
}
