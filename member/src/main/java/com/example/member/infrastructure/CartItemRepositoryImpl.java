package com.example.member.infrastructure;

import com.example.member.application.CartItemRepository;
import com.example.member.domain.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemJpaRepository.save(CartItemEntity.from(cartItem)).toModel();
    }

    @Override
    public CartItem findById(Long cartItemId) {
        return cartItemJpaRepository.findById(cartItemId)
                .map(CartItemEntity::toModel)
                .orElseThrow(() -> new IllegalArgumentException(
                        "cart item not fount -> cartItemId: " + cartItemId)
                );
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemJpaRepository.delete(CartItemEntity.from(cartItem));
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        return cartItemJpaRepository.findAllByMemberId(memberId)
                .stream().map(CartItemEntity::toModel).toList();
    }

    @Override
    public void deleteAll(List<CartItem> cartItems) {
        cartItemJpaRepository.deleteAll(cartItems.stream().map(CartItemEntity::from).toList());
    }

}
