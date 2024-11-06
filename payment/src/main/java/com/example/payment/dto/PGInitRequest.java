package com.example.payment.dto;

import java.math.BigDecimal;

public record PGInitRequest(
        Long orderId,
        BigDecimal amount,
        MemberPaymentInfo memberPaymentInfo
) {
}
