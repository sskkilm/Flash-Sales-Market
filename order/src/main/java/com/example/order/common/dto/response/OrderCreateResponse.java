package com.example.order.common.dto.response;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;

import java.math.BigDecimal;

public record OrderCreateResponse(
        Long orderId,
        OrderStatus status,
        BigDecimal amount
) {
    public static OrderCreateResponse from(Order order, BigDecimal amount) {
        return new OrderCreateResponse(
                order.getId(),
                order.getStatus(),
                amount
        );
    }
}
