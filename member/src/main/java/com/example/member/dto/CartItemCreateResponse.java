package com.example.member.dto;

import com.example.member.domain.CartItem;

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
