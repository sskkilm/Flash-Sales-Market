package com.example.order.common.dto.response;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;

public record OrderCreateResponse(
        Long orderId,
        Long memberId,
        OrderStatus status
) {
    public static OrderCreateResponse from(Order order) {
        return new OrderCreateResponse(
                order.getId(),
                order.getMemberId(),
                order.getStatus()
        );
    }
}
