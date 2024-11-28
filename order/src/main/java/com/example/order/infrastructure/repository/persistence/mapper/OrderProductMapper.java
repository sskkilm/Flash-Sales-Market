package com.example.order.infrastructure.repository.persistence.mapper;

import com.example.order.domain.OrderProduct;
import com.example.order.infrastructure.repository.persistence.entity.OrderProductEntity;

public class OrderProductMapper {

    public static OrderProductEntity toEntity(OrderProduct orderProduct) {
        return OrderProductEntity.builder()
                .id(orderProduct.getId())
                .order(OrderMapper.toEntity(orderProduct.getOrder()))
                .productId(orderProduct.getProductId())
                .name(orderProduct.getName())
                .quantity(orderProduct.getQuantity())
                .orderAmount(orderProduct.getAmount())
                .createdAt(orderProduct.getCreatedAt())
                .build();
    }

    public static OrderProduct toModel(OrderProductEntity orderProductEntity) {
        return OrderProduct.builder()
                .id(orderProductEntity.getId())
                .order(OrderMapper.toModel(orderProductEntity.getOrder()))
                .productId(orderProductEntity.getProductId())
                .name(orderProductEntity.getName())
                .quantity(orderProductEntity.getQuantity())
                .amount(orderProductEntity.getOrderAmount())
                .createdAt(orderProductEntity.getCreatedAt())
                .build();
    }
}
