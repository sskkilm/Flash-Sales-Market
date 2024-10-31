package com.example.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductOrderInfo(
        @NotNull Long productId,
        @Min(1) int quantity
) {
}
