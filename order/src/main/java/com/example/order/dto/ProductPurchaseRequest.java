package com.example.order.dto;

public record ProductPurchaseRequest(
        Long productId,
        int quantity
) {
}