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
    private String name;
    private Money orderAmount;
    private LocalDateTime createdAt;

    public static OrderProduct create(Order order, Long productId, int quantity, String name, Money orderAmount) {
        return OrderProduct.builder()
                .order(order)
                .productId(productId)
                .quantity(quantity)
                .name(name)
                .orderAmount(orderAmount)
                .build();
    }
}
