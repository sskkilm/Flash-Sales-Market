package com.port90.cartitem.common.dto.request;

public record CartItemOrderRequest(
        Long productId,
        int quantity
) {
}
