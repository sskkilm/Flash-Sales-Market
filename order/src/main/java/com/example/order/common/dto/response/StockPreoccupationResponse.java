package com.example.order.common.dto.response;

import com.example.order.common.dto.StockPreoccupationResult;

import java.util.List;

public record StockPreoccupationResponse(
        List<StockPreoccupationResult> stockPreoccupationResults
) {
}
