package com.example.product.dto;

import java.math.BigDecimal;

public record StockPreoccupationResult(
        Long productId, String productName, int quantity, BigDecimal price
) {
}
