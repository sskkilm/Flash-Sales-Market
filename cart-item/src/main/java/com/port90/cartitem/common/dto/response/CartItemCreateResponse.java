package com.port90.cartitem.common.dto.response;

import com.port90.cartitem.domain.model.CartItem;

public record CartItemCreateResponse(
        Long cartItemId,
        Long memberId,
        Long productId,
        int quantity
) {
    public static CartItemCreateResponse from(CartItem cartItem) {
        return new CartItemCreateResponse(
                cartItem.getId(),
                cartItem.getMemberId(),
                cartItem.getProductId(),
                cartItem.getQuantity()
        );
    }
}
