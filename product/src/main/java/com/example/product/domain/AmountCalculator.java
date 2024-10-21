package com.example.product.domain;

import com.example.common.domain.Money;
import org.springframework.stereotype.Component;

@Component
public class AmountCalculator {
    public Money calculateAmount(Product product, int quantity) {
        return product.getPrice().multiply(quantity);
    }
}
