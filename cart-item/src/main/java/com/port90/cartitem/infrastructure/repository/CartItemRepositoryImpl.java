package com.port90.cartitem.infrastructure.repository;

import com.port90.cartitem.application.port.CartItemRepository;
import com.port90.cartitem.domain.exception.CartItemException;
import com.port90.cartitem.domain.model.CartItem;
import com.port90.cartitem.infrastructure.repository.persistence.CartItemJpaRepository;
import com.port90.cartitem.infrastructure.repository.persistence.mapper.CartItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.port90.cartitem.domain.exception.ErrorCode.CART_ITEM_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class CartItemRepositoryImpl implements CartItemRepository {

    private final CartItemJpaRepository cartItemJpaRepository;

    @Override
    public CartItem save(CartItem cartItem) {
        return CartItemMapper.toModel(
                cartItemJpaRepository.save(
                        CartItemMapper.toEntity(cartItem)
                )
        );
    }

    @Override
    public CartItem findById(Long cartItemId) {
        return cartItemJpaRepository.findById(cartItemId)
                .map(CartItemMapper::toModel)
                .orElseThrow(() -> new CartItemException(CART_ITEM_NOT_FOUND));
    }

    @Override
    public void delete(CartItem cartItem) {
        cartItemJpaRepository.delete(
                CartItemMapper.toEntity(cartItem)
        );
    }

    @Override
    public List<CartItem> findAllByMemberId(Long memberId) {
        return cartItemJpaRepository.findAllByMemberId(memberId)
                .stream()
                .map(CartItemMapper::toModel)
                .toList();
    }

    @Override
    public void deleteAll(List<CartItem> cartItems) {
        cartItemJpaRepository.deleteAll(
                cartItems
                        .stream()
                        .map(CartItemMapper::toEntity).toList()
        );
    }

}
