package com.example.product.dto;

import java.math.BigDecimal;

public record StockHoldResult(
        Long productId, String productName, int quantity, BigDecimal amount
) {
}
