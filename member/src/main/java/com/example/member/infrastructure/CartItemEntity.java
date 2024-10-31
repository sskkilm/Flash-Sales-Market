package com.example.member.infrastructure;

import com.example.member.domain.CartItem;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "CartItem")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private int quantity;

    @CreatedDate()
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static CartItemEntity from(CartItem cartItem) {
        return CartItemEntity.builder()
                .id(cartItem.getId())
                .memberId(cartItem.getMemberId())
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .createdAt(cartItem.getCreatedAt())
                .updatedAt(cartItem.getUpdatedAt())
                .build();
    }

    public CartItem toModel() {
        return CartItem.builder()
                .id(this.id)
                .memberId(this.memberId)
                .productId(this.productId)
                .quantity(this.quantity)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
