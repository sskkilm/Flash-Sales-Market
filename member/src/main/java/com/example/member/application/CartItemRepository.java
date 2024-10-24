package com.example.member.application;

import com.example.member.domain.CartItem;

import java.util.Optional;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    Optional<CartItem> findById(Long cartItemId);

    void delete(CartItem cartItem);
}
