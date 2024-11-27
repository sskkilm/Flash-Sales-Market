package com.port90.cartitem.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class CartItem {

    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CartItem create(Long memberId, Long productId, int quantity) {
        return CartItem.builder()
                .memberId(memberId)
                .productId(productId)
                .quantity(quantity)
                .build();
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isNotIncludedBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }
}
