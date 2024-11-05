package com.example.product.domain;

import java.math.BigDecimal;

public class AmountCalculator {
    public BigDecimal calculate(Product product, int quantity) {
        return product.getPrice().multiply(new BigDecimal(String.valueOf(quantity)));
    }
}
