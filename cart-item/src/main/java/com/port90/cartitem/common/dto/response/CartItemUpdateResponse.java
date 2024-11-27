package com.port90.cartitem.common.dto.response;

import com.port90.cartitem.domain.model.CartItem;

public record CartItemUpdateResponse(
        Long cartItemId,
        Long memberId,
        Long productId,
        int quantity
) {
    public static CartItemUpdateResponse from(CartItem cartItem) {
        return new CartItemUpdateResponse(
                cartItem.getId(),
                cartItem.getMemberId(),
                cartItem.getProductId(),
                cartItem.getQuantity()
        );
    }
}
