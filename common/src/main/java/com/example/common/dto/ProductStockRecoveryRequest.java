package com.example.common.dto;

public record ProductStockRecoveryRequest(
        Long productId, int quantity
) {
}
