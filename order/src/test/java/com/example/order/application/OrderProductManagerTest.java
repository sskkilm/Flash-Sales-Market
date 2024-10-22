package com.example.order.application;

import com.example.common.domain.Money;
import com.example.order.domain.OrderProduct;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderProductManagerTest {

    @Test
    void 주문_상품들의_총_금액을_계산한다() {
        //given
        OrderProductManager manager = new OrderProductManager();

        List<OrderProduct> orderProducts = List.of(
                OrderProduct.builder()
                        .orderAmount(Money.of("10000"))
                        .build(),
                OrderProduct.builder()
                        .orderAmount(Money.of("20000"))
                        .build(),
                OrderProduct.builder()
                        .orderAmount(Money.of("30000"))
                        .build()
        );

        //when
        BigDecimal totalPrice = manager.calculateTotalPrice(orderProducts);

        //then
        assertEquals(new BigDecimal("60000"), totalPrice);
    }

}