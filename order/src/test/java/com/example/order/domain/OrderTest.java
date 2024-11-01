package com.example.order.domain;

import com.example.order.exception.CanNotBeCanceledException;
import com.example.order.exception.CanNotBeReturnedException;
import com.example.order.exception.OrderMemberUnmatchedException;
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
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
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
        assertThrows(OrderMemberUnmatchedException.class,
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
        assertThrows(CanNotBeCanceledException.class,
                //when
                () -> order.cancel(1L, null));
    }

    @Test
    void 주문_취소_시_취소_가능_기간이_지났으면_예외가_발생한다() {
        //given
        LocalDateTime orderCompleteDatetime = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED)
                .createdAt(orderCompleteDatetime)
                .build();
        LocalDateTime canceledDateTime = orderCompleteDatetime.plusDays(1).plusNanos(1);

        //then
        assertThrows(CanNotBeCanceledException.class,
                //when
                () -> order.cancel(1L, canceledDateTime));
    }

    @Test
    void 주문_취소() {
        //given
        LocalDateTime now = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED)
                .createdAt(now)
                .build();
        LocalDateTime canceledDateTime = now.plusDays(1).minusNanos(1);

        //when
        order.cancel(1L, canceledDateTime);

        //then
        assertEquals(OrderStatus.CANCELED, order.getStatus());
    }

    @Test
    void 반품_시_회원_정보가_일치하지_않으면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //then
        assertThrows(OrderMemberUnmatchedException.class,
                //when
                () -> order.returns(2L, null));
    }

    @Test
    void 반품_시_배송_완료_상태가_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED)
                .build();

        //then
        assertThrows(CanNotBeReturnedException.class,
                //when
                () -> order.returns(1L, null));
    }

    @Test
    void 반품_시_반품_가능_기간이_지났으면_예외가_발생한다() {
        //given
        LocalDateTime deliveryCompletedDateTime = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusDays(1).plusNanos(1);

        //then
        assertThrows(CanNotBeReturnedException.class,
                //when
                () -> order.returns(1L, returnedDateTime));
    }

    @Test
    void 반품() {
        //given
        LocalDateTime deliveryCompletedDateTime = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        Order order = Order.builder()
                .memberId(1L)
                .status(OrderStatus.DELIVERED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusDays(1).minusNanos(1);

        //when
        order.returns(1L, returnedDateTime);

        //then
        assertEquals(OrderStatus.RETURN_IN_PROGRESS, order.getStatus());
    }

    @Test
    void 반품_완료() {
        //given
        Order order = Order.builder()
                .status(OrderStatus.RETURN_IN_PROGRESS)
                .build();

        //when
        order.returned();

        //then
        assertEquals(OrderStatus.RETURNED, order.getStatus());
    }
}