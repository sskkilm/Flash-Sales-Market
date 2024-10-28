package com.example.order.dto;

public record OrderReturnResponse(
        Long orderId,
        Long memberId,
        String status
) {
}
