package com.example.payment.dto;

import java.math.BigDecimal;

public record PGPaymentResponse(
        Long orderId,
        BigDecimal amount,
        String paymentKey
) {
}
