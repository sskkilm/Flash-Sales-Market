package com.example.order.dto;

import java.math.BigDecimal;

public record OrderedProductInfo(
        Long productId,
        String productName,
        int quantity,
        BigDecimal amount
) {
}
