package com.example.order.dto;

import com.example.common.domain.Money;
import com.example.order.domain.OrderProduct;

public record OrderProductDto(
        Long productId,
        int quantity,
        String productName,
        Money orderAmount
) {
    public static OrderProductDto from(OrderProduct orderProduct) {
        return new OrderProductDto(
                orderProduct.getProductId(),
                orderProduct.getQuantity(),
                orderProduct.getName(),
                orderProduct.getOrderAmount()
        );
    }
}
