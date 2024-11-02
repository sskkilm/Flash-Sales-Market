package com.example.order.dto;

public record ProductRestockInfo(
        Long productId,
        int quantity
) {
}
