package com.example.order.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderValidationRequest(
        @NotNull Long orderId,
        @NotNull BigDecimal amount
) {
}
