package com.example.order.common.dto.request;

public record CartItemOrderRequest(
        Long productId,
        int quantity
) {
}
