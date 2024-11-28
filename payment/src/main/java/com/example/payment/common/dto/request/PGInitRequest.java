package com.example.payment.common.dto.request;

import java.math.BigDecimal;

public record PGInitRequest(
        Long orderId,
        BigDecimal amount
) {
}
