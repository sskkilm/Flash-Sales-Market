package com.example.order.dto;

import com.example.common.domain.Money;
import com.example.order.domain.OrderProduct;

public record OrderProductResponse(
        Long orderProductId,
        String productName,
        int quantity,
        Money orderAmount
) {
    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderAmount()
        );
    }
}
