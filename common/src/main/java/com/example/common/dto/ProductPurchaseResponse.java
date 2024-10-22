package com.example.common.dto;

import com.example.common.domain.Money;

public record ProductPurchaseResponse(
        Long productId, int quantity, String name, Money purchaseAmount
) {
}
