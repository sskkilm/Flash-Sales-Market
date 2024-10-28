package com.example.product.dto;

import java.math.BigDecimal;

public record ProductFeignResponse(
        Long productId,
        String name,
        BigDecimal price
) {
}
