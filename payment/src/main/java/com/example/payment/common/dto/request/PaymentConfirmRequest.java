package com.example.payment.common.dto.request;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentConfirmRequest(
        @NotNull String paymentKey,
        @NotNull Long orderId,
        @NotNull BigDecimal amount,
        boolean flag
) {
}
