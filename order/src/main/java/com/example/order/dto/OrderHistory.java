package com.example.order.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderHistory(
        Long orderId,
        Long memberId,
        String status,
        BigDecimal totalPrice,
        List<OrderProductResponse> orderProducts
) {
}
