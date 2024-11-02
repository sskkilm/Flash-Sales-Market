package com.example.order.dto;

import java.math.BigDecimal;

public record OrderCreateResponse(
        Long orderId,
        Long memberId,
        String status,
        BigDecimal totalAmount
) {
}
