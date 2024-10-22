package com.example.order.application;

import com.example.order.domain.OrderProduct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderProductManager {

    public BigDecimal calculateTotalPrice(List<OrderProduct> orderProducts) {
        return orderProducts.stream()
                .map(orderProduct -> orderProduct.getOrderAmount().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
