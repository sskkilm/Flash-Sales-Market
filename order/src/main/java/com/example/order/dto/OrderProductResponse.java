package com.example.order.dto;

import com.example.common.domain.Money;
import com.example.order.domain.OrderProduct;

public record OrderProductResponse(
        Long productId,
        int quantity,
        String productName,
        Money orderAmount
) {
    public static OrderProductResponse from(OrderProduct orderProduct) {
        return new OrderProductResponse(
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getName(),
                orderProduct.getOrderAmount()
        );
    }
}
