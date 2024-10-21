package com.example.order.dto;

import com.example.common.domain.Money;

public record OrderProductResponse(
        Long productId,
        int quantity,
        Money orderAmount
) {
}
