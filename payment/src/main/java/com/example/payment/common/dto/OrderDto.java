package com.example.payment.common.dto;

import java.math.BigDecimal;

public record OrderDto(
        Long orderId,
        Long memberId,
        String status,
        BigDecimal amount
) {
}
