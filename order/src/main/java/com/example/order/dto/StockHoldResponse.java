package com.example.order.dto;

import java.util.List;

public record StockHoldResponse(
        List<StockHoldResult> stockHoldResults
) {
}
