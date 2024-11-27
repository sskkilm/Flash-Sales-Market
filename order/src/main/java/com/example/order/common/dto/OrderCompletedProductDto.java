package com.example.order.common.dto;

import com.example.order.domain.OrderProduct;

public record OrderCompletedProductDto(
        Long productId,
        int quantity
) {
    public static OrderCompletedProductDto from(OrderProduct orderProduct) {
        return new OrderCompletedProductDto(orderProduct.getProductId(), orderProduct.getQuantity());
    }
}
