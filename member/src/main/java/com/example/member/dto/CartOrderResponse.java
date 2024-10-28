package com.example.member.dto;

import java.util.List;

public record CartOrderResponse(
        Long orderId,
        Long memberId,
        String status,
        List<CartItemOrderResponse> orderProducts
) {
}
