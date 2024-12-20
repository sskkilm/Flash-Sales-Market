package com.example.order.common.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderInfo(
        @NotNull Long productId,
        @Min(1) int quantity
) {
}
