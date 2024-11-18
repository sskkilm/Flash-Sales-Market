package com.example.payment.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record PaymentInitRequest(
        @NotNull Long orderId,
        @NotNull List<Long> orderProductIds,
        @NotNull BigDecimal amount,
        @NotNull MemberPaymentInfo memberPaymentInfo
) {
}
