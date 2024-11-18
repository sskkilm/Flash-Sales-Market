package com.example.product.dto;

import java.util.List;

public record StockPreoccupationResponse(
        List<StockPreoccupationResult> stockPreoccupationResults
) {
}
