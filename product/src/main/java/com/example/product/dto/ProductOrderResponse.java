package com.example.product.dto;

import java.util.List;

public record ProductOrderResponse(
        List<OrderedProductInfo> orderedProductInfos
) {
}
