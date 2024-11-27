package com.example.order.common.dto.response;

import java.util.List;

public record CartOrderResponse(
        Long orderId,
        Long memberId,
        List<Long> orderProductIds
) {
    public static CartOrderResponse from(Long memberId, OrderCreateResponse response) {
        return new CartOrderResponse(
                response.orderId(),
                memberId,
                response.orderProductIds()
        );
    }
}
