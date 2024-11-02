package com.example.order.dto;

import java.util.List;

public record ProductOrderResponse(
        List<OrderedProductInfo> orderedProductInfos
) {
}
