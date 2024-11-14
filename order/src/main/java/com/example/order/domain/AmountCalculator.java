package com.example.order.domain;

import java.math.BigDecimal;
import java.util.List;

public class AmountCalculator {

    public BigDecimal calculateTotalAmount(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(OrderProduct::getOrderAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
