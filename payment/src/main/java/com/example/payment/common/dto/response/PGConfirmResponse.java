package com.example.payment.common.dto.response;

import java.math.BigDecimal;

public record PGConfirmResponse(
        String paymentKey,
        Long orderId,
        BigDecimal amount
) {
}
