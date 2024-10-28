package com.example.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductPurchaseFeignRequest(
        @NotNull Long productId,
        @Min(1) int quantity) {
}