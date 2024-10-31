package com.example.order.dto;

public record ProductRestoreStockInfo(
        Long productId,
        int quantity
) {
}
