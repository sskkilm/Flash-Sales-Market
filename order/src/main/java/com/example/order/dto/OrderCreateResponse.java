package com.example.order.dto;

import java.util.List;

public record OrderCreateResponse(
        Long orderId,
        Long memberId,
        String status,
        List<OrderedProductInfo> orderedProductInfos
) {
}
