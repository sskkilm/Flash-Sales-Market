package com.example.member.infrastructure;

import com.example.member.application.CartItemRepository;
import com.example.member.domain.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        return cartItemJpaRepository.save(CartItemEntity.from(cartItem)).toModel();
    }

    @Override
    public Optional<CartItem> findById(Long cartItemId) {
        return cartItemJpaRepository.findById(cartItemId)
                .map(CartItemEntity::toModel);
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemJpaRepository.delete(CartItemEntity.from(cartItem));
    }

}
