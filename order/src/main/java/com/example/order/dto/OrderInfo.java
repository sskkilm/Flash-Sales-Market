package com.example.order.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderInfo(
        @NotNull Long orderId,
        @NotNull BigDecimal totalAmount
) {
}
