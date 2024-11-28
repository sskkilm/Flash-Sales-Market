package com.example.order.domain;

import java.math.BigDecimal;
import java.util.List;

public class AmountCalculator {

    public BigDecimal calculateTotalAmount(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderProduct::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateAmount(int quantity, BigDecimal price) {

        return price.multiply(new BigDecimal(String.valueOf(quantity)));
    }
}
