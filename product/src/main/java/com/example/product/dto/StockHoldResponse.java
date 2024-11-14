package com.example.product.dto;

import java.util.List;

public record StockHoldResponse(
        List<StockHoldResult> stockHoldResults
) {
}
