package com.example.order.dto;

import java.util.List;

public record StockHoldRequest(
        Long orderId,
        List<StockHoldInfo> stockHoldInfos
) {
}
