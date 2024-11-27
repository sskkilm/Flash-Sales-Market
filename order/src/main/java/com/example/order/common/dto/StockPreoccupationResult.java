package com.example.order.common.dto;

import java.math.BigDecimal;

public record StockPreoccupationResult(
        Long productId,
        String productName,
        int quantity,
        BigDecimal amount
) {
}
