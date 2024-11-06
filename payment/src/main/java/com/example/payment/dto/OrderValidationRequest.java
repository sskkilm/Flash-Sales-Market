package com.example.payment.dto;

import java.math.BigDecimal;

public record OrderValidationRequest(
        Long orderId,
        BigDecimal amount
) {
}
