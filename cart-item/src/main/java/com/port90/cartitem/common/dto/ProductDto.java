package com.port90.cartitem.common.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long productId,
        String name,
        BigDecimal price
) {
}
