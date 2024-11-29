package com.example.order.infrastructure.repository.persistence.mapper;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;
import com.example.order.infrastructure.repository.persistence.entity.OrderEntity;

public class OrderMapper {

    public static OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .memberId(order.getMemberId())
                .status(order.getStatus().toString())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    public static Order toModel(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .memberId(orderEntity.getMemberId())
                .status(OrderStatus.valueOf(orderEntity.getStatus()))
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();
    }
}
