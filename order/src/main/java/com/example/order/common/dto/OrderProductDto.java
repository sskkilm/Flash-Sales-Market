package com.example.order.common.dto;

import com.example.order.domain.OrderProduct;

import java.math.BigDecimal;

public record OrderProductDto(
        Long orderProductId,
        Long productId,
        String productName,
        int quantity,
        BigDecimal orderAmount
) {

    public static OrderProductDto from(OrderProduct orderProduct) {
        return new OrderProductDto(
                orderProduct.getId(),
                orderProduct.getProductId(),
                orderProduct.getName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderAmount()
        );
    }
}
