package com.example.product.domain;

import java.math.BigDecimal;

public record Money(BigDecimal price) {

    public static Money of(BigDecimal price) {
        return new Money(price);
    }
}
