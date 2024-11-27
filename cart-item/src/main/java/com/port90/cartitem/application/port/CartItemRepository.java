package com.port90.cartitem.application.port;

import com.port90.cartitem.domain.model.CartItem;

import java.util.List;

public interface CartItemRepository {
    CartItem save(CartItem cartItem);

    CartItem findById(Long cartItemId);

    void delete(CartItem cartItem);

    List<CartItem> findAllByMemberId(Long memberId);

    void deleteAll(List<CartItem> cartItems);
}
