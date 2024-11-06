package com.example.payment.dto;

import java.math.BigDecimal;

public record PaymentConfirmResponse(
        Long paymentId,
        Long orderId,
        BigDecimal amount,
        String paymentKey
) {
}
