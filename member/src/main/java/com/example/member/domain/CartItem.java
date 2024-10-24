package com.example.member.domain;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
}
