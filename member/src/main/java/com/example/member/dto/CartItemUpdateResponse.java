package com.example.member.dto;

import com.example.member.domain.CartItem;

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
