package com.example.order.application;

import com.example.order.domain.OrderProduct;
import com.example.order.domain.AmountCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AmountCalculatorTest {

    @Test
    void 주문_상품들의_총_금액을_계산한다() {
        //given
        AmountCalculator manager = new AmountCalculator();

        List<OrderProduct> orderProducts = List.of(
                OrderProduct.builder()
                        .orderAmount(new BigDecimal("10000"))
                        .build(),
                OrderProduct.builder()
                        .orderAmount(new BigDecimal("20000"))
                        .build(),
                OrderProduct.builder()
                        .orderAmount(new BigDecimal("30000"))
                        .build()
        );

        //when

        BigDecimal totalPrice = manager.calculateTotalAmount(orderProducts);

        //then
        assertEquals(new BigDecimal("60000"), totalPrice);
    }

}