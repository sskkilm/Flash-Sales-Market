package com.example.product.dto;

import java.math.BigDecimal;

public record ProductPurchaseFeignResponse(
        Long productId, String productName, int quantity, BigDecimal purchaseAmount
) {
}
