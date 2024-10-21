package com.example.order.domain;

import com.example.common.domain.Money;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OrderProduct {
    private Long id;
    private Order order;
    private Long productId;
    private int quantity;
    private Money orderAmount;
    private LocalDateTime createdAt;

    public static OrderProduct create(Order order, Long productId, int quantity, Money orderAmount) {
        return OrderProduct.builder()
                .order(order)
                .productId(productId)
                .quantity(quantity)
                .orderAmount(orderAmount)
                .build();
    }
}