package com.example.order.dto;

import java.util.List;

public record ProductOrderRequest(
        Long orderId,
        List<ProductOrderInfo> productOrderInfos
) {
}
