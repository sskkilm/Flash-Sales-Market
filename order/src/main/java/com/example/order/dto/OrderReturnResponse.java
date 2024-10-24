package com.example.order.dto;

import com.example.order.domain.OrderStatus;

public record OrderReturnResponse(
        Long orderId,
        Long memberId,
        OrderStatus status
) {
}
