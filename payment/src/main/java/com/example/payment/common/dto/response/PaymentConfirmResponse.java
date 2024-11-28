package com.example.payment.common.dto.response;

import java.math.BigDecimal;

public record PaymentConfirmResponse(
        Long paymentId,
        Long orderId,
        BigDecimal amount,
        String paymentKey
) {
}
