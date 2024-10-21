package com.example.order.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTest {

    @Test
    void 주문_생성() {
        //given
        Long memberId = 1L;

        //when
        Order order = Order.create(memberId);

        //then
        assertNull(order.getId());
        assertEquals(1L, order.getMemberId());
        assertEquals(OrderStatus.ORDER_COMPLETED, order.getStatus());
        assertNull(order.getCreatedAt());
        assertNull(order.getUpdatedAt());
    }
}