package com.port90.cartitem.common.dto.response;

public record OrderCreateResponse(
        Long orderId,
        Long memberId,
        String status
) {
}
