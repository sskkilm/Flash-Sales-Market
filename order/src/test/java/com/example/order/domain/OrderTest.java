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
    void 주문_취소_시_회원_정보가_일치하지_않으면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> order.cancel(2L));
    }

    @Test
    void 주문_취소_시_배송_전이_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERY_IN_PROGRESS)
                .build();

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> order.cancel(1L));
    }

    @Test
    void 주문_취소() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();

        //then
        order.cancel(1L);
    }
}