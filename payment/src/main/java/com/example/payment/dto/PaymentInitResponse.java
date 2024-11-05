package com.example.payment.dto;

import java.math.BigDecimal;

public record PaymentInitResponse(
        Long paymentId,
        Long orderId,
        BigDecimal totalAmount,
        String paymentKey
) {
}
