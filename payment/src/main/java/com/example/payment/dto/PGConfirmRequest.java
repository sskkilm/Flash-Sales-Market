package com.example.payment.dto;

import java.math.BigDecimal;

public record PGConfirmRequest(
        String paymentKey,
        Long orderId,
        BigDecimal amount,
        MemberPaymentInfo memberPaymentInfo
) {
}
