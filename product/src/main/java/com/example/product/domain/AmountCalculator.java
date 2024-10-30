package com.example.product.domain;

import java.math.BigDecimal;

public class AmountCalculator {
    public BigDecimal calculateAmount(Product product, int quantity) {
        return product.getPrice().multiply(new BigDecimal(String.valueOf(quantity)));
    }
}
