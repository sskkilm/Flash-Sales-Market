package com.example.order.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
}
