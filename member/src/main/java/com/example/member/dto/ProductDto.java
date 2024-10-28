package com.example.member.dto;

import java.math.BigDecimal;

public record ProductDto(
        Long productId,
        String name,
        BigDecimal price
) {
}
