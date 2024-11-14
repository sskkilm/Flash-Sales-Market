package com.example.order.dto;

public record StockHoldInfo(
        Long productId,
        int quantity
) {
}