package com.example.member.dto;

import java.math.BigDecimal;

public record CartItemOrderResponse(
        Long orderProductId,
        String productName,
        int quantity,
        BigDecimal orderAmount
) {
}
