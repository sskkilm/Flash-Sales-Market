package com.example.order.dto;

import com.example.order.domain.OrderStatus;

import java.math.BigDecimal;
import java.util.List;

public record OrderHistory(
        Long orderId,
        Long memberId,
        OrderStatus status,
        BigDecimal totalPrice,
        List<OrderProductResponse> orderProducts
) {
}
