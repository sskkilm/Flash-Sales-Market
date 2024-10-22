package com.example.order.dto;

import com.example.order.domain.OrderStatus;

import java.util.List;

public record OrderHistory(
        Long orderId,
        Long memberId,
        OrderStatus status,
        String totalPrice,
        List<OrderProductDto> orderProducts
) {
}
