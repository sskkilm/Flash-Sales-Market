package com.example.order.dto;

import com.example.order.domain.OrderStatus;

import java.util.List;

public record OrderCancelResponse(
        Long orderId,
        Long memberId,
        OrderStatus status,
        List<OrderProductDto> orderProducts
) {
}
