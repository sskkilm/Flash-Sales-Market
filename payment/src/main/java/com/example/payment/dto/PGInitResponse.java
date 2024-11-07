package com.example.payment.dto;

import java.math.BigDecimal;

public record PGInitResponse(
        String paymentKey,
        Long orderId,
        BigDecimal amount,
        MemberPaymentInfo memberPaymentInfo
) {
}
