package com.example.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Builder
public class Order {
    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Order create(Long memberId) {
        return Order.builder()
                .memberId(memberId)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();
    }

    public boolean isNotOrderedBy(Long memberId) {
        return !Objects.equals(this.memberId, memberId);
    }

    public boolean canNotBeCanceled() {
        return this.status != OrderStatus.ORDER_COMPLETED;
    }

    public void canceled() {
        this.status = OrderStatus.CANCEL_COMPLETED;
    }
}
