package com.example.product.common.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record StockIncreaseRequest(
        @NotNull Long productId,
        @Min(1) int quantity
) {
}
