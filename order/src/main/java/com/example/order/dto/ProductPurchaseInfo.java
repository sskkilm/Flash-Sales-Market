package com.example.order.dto;

public record ProductPurchaseInfo(
        Long productId,
        int quantity
) {
}