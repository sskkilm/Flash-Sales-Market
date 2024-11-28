package com.example.payment.common.dto.request;

import java.math.BigDecimal;

public record PGConfirmRequest(
        String paymentKey,
        Long orderId,
        BigDecimal amount
) {
}
