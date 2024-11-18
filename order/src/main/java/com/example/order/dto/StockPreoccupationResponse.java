package com.example.order.dto;

import java.util.List;

public record StockPreoccupationResponse(
        List<StockPreoccupationResult> stockPreoccupationResults
) {
}
