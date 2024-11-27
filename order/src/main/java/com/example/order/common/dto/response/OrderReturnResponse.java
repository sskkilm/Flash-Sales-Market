package com.example.order.common.dto.response;

public record OrderReturnResponse(
        Long orderId,
        Long memberId,
        String status
) {
}
