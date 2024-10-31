package com.example.member.application;

import com.example.member.domain.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    CartItem findById(Long cartItemId);

    void delete(CartItem cartItem);

    List<CartItem> findAllByMemberId(Long memberId);

    void deleteAll(List<CartItem> cartItems);
}
