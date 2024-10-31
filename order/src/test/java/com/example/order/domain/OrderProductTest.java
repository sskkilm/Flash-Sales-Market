package com.example.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderProductTest {

    @Test
    void 주문_상품을_생성한다() {
        //given
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();

        //when
        OrderProduct orderProduct = OrderProduct.create(
                order, 1L, "name", 10, new BigDecimal("10000")
        );

        //then
        assertNull(orderProduct.getId());
        assertEquals(order, orderProduct.getOrder());
        assertEquals(1L, orderProduct.getProductId());
        assertEquals("name", orderProduct.getName());
        assertEquals(10, orderProduct.getQuantity());
        assertEquals(new BigDecimal("10000"), orderProduct.getOrderAmount());
        assertNull(orderProduct.getCreatedAt());
    }
}