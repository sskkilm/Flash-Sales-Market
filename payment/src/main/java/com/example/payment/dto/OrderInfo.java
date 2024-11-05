package com.example.payment.dto;

import java.math.BigDecimal;

public record OrderInfo(
        Long orderId,
        BigDecimal totalAmount
) {
}
