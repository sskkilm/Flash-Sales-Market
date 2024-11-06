package com.example.payment.dto;

import java.math.BigDecimal;

public record PGConfirmResponse(
        Long orderId,
        BigDecimal amount,
        String paymentKey
) {
}
