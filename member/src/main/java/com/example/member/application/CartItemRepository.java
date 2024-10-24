package com.example.member.application;

import com.example.member.domain.CartItem;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);
}
