package com.port90.cartitem.infrastructure.repository.persistence.mapper;

import com.port90.cartitem.domain.model.CartItem;
import com.port90.cartitem.infrastructure.repository.persistence.entity.CartItemEntity;

public class CartItemMapper {

    public static CartItemEntity toEntity(CartItem cartItem) {
        return CartItemEntity.builder()
                .id(cartItem.getId())
                .memberId(cartItem.getMemberId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }

    public static CartItem toModel(CartItemEntity cartItemEntity) {
        return CartItem.builder()
                .id(cartItemEntity.getId())
                .memberId(cartItemEntity.getMemberId())
                .productId(cartItemEntity.getProductId())
                .quantity(cartItemEntity.getQuantity())
                .createdAt(cartItemEntity.getCreatedAt())
                .updatedAt(cartItemEntity.getUpdatedAt())
                .build();
    }
}
