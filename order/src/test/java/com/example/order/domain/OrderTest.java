package com.example.order.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void 회원의_주문이_맞는지_확인() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //when
        boolean orderNotOrderedBy = order.isNotOrderedBy(1L);

        //then
        assertFalse(orderNotOrderedBy);
    }

    @Test
    void 취소될_수_있는_주문인지_확인() {
        //given
        Order order = Order.builder()
                .status(OrderStatus.ORDER_COMPLETED)
                .build();

        //when
        boolean canNotBeCanceled = order.canNotBeCanceled();

        //then
        assertFalse(canNotBeCanceled);
    }
}