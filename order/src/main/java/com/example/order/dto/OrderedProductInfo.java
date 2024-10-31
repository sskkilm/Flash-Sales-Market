package com.example.order.dto;

import com.example.order.domain.OrderProduct;

import java.math.BigDecimal;

public record OrderedProductInfo(
        Long orderProductId,
        String productName,
        int quantity,
        BigDecimal orderAmount
) {
    public static OrderedProductInfo from(OrderProduct orderProduct) {
        return new OrderedProductInfo(
                orderProduct.getId(),
                orderProduct.getName(),
                orderProduct.getQuantity(),
                orderProduct.getOrderAmount()
        );
    }
}
