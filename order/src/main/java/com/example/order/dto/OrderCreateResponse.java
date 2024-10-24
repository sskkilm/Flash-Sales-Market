package com.example.order.dto;

import com.example.order.domain.OrderStatus;

import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        Long memberId,
        OrderStatus status,
        List<OrderProductResponse> orderProducts
) {
}
