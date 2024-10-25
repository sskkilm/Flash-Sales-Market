package com.example.order.dto;

import com.example.order.domain.OrderProduct;

import java.math.BigDecimal;

public record OrderProductResponse(
        Long orderProductId,
        String productName,
        int quantity,
        BigDecimal orderAmount
) {
    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderAmount().amount()
        );
    }
}
