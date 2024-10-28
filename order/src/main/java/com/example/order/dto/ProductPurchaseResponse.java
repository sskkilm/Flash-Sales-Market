package com.example.order.dto;

import java.math.BigDecimal;

public record ProductPurchaseResponse(
        Long productId, String productName, int quantity, BigDecimal purchaseAmount
) {
}
