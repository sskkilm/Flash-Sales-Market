package com.example.order.dto;

public record OrderCancelResponse(
        Long orderId,
        Long memberId,
        String status
) {
}
