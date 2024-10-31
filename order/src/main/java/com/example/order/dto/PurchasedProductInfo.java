package com.example.order.dto;

import java.math.BigDecimal;

public record PurchasedProductInfo(
        Long productId, String productName, int quantity, BigDecimal purchaseAmount
) {
}
