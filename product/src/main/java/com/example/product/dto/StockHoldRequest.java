package com.example.product.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record StockHoldRequest(
        @NotNull Long orderId,
        @NotEmpty List<StockHoldInfo> stockHoldInfos
) {
}
