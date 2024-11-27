package com.example.order.common.dto.response;

public record OrderCancelResponse(
        Long orderId,
        Long memberId,
        String status
) {
}
