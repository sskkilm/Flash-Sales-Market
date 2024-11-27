package com.example.order.common.dto;

public record ProductRestockInfo(
        Long productId,
        int quantity
) {
}
