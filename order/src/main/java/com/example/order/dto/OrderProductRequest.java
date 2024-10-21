package com.example.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderProductRequest(
        @NotNull Long productId,
        @Min(1) int quantity
) {
}
