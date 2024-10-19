package com.example.product.domain;

import java.math.BigDecimal;

public record Money(BigDecimal amount) {
    
    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }
}
