package com.example.order.domain;

import com.example.order.application.AppLocalDateTimeHolder;
import com.example.order.application.LocalDateTimeHolder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

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
                () -> order.cancel(2L, null));
    }

    @Test
    void 주문_취소_시_주문_완료_상태가_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERY_IN_PROGRESS)
                .build();

        //then
        assertThrows(IllegalStateException.class,
                //when
                () -> order.cancel(1L, null));
    }

    @Test
    void 주문_취소_시_취소_가능_기간이_지났으면_예외가_발생한다() {
        //given
        LocalDateTime orderDateTime = LocalDateTime.now();
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .createdAt(orderDateTime)
                .build();
        LocalDateTimeHolder holder = () -> orderDateTime.plusDays(1).plusNanos(1);

        //then
        assertThrows(IllegalStateException.class,
                //when
                () -> order.cancel(1L, holder));
    }

    @Test
    void 주문_취소() {
        //given
        LocalDateTime now = LocalDateTime.now();
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .createdAt(now)
                .build();
        LocalDateTimeHolder holder = new AppLocalDateTimeHolder();

        //then
        order.cancel(1L, holder);
    }

    @Test
    void 반품_시_회원_정보가_일치하지_않으면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> order.returns(2L, null));
    }

    @Test
    void 반품_시_배송_완료_상태가_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();

        //then
        assertThrows(IllegalStateException.class,
                //when
                () -> order.returns(1L, null));
    }

    @Test
    void 반품_시_반품_가능_기간이_지났으면_예외가_발생한다() {
        //given
        LocalDateTime deliveryCompletedDateTime = LocalDateTime.now();
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERY_COMPLETED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTimeHolder holder = () -> deliveryCompletedDateTime.plusDays(1).plusNanos(1);

        //then
        assertThrows(IllegalStateException.class,
                //when
                () -> order.returns(1L, holder));
    }

    @Test
    void 반품() {
        //given
        LocalDateTime deliveryCompletedDateTime = LocalDateTime.now();
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERY_COMPLETED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTimeHolder holder = () -> deliveryCompletedDateTime.plusDays(1).minusNanos(1);

        //when
        order.returns(1L, holder);
    }
}