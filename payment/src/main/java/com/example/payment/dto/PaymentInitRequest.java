package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentInitRequest(
        @NotNull Long orderId,
        @NotNull BigDecimal amount,
        @NotNull MemberPaymentInfo memberPaymentInfo
) {
}
