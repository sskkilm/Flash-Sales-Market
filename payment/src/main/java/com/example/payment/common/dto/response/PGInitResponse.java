package com.example.payment.common.dto.response;

import com.example.payment.common.dto.MemberPaymentInfo;

import java.math.BigDecimal;

public record PGInitResponse(
        String paymentKey,
        Long orderId,
        BigDecimal amount,
        MemberPaymentInfo memberPaymentInfo
) {
}
