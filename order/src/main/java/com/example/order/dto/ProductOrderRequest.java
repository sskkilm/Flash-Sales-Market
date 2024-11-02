package com.example.order.dto;

import java.util.List;

public record ProductOrderRequest(
        List<ProductOrderInfo> productOrderInfos
) {
}
