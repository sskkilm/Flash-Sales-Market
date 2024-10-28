package com.example.order.dto;

public record ProductRestoreStockRequest(
        Long productId,
        int quantity
) {
}
