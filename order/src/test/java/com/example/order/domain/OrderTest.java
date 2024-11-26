package com.example.order.domain;

import com.example.order.domain.exception.OrderServiceException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static com.example.order.domain.OrderStatus.*;
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
        assertEquals(PENDING, order.getStatus());
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
        assertThrows(OrderServiceException.class,
                //when
                () -> order.cancel(2L, null));
    }

    @Test
    void 주문_취소_시_주문_완료_상태가_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(DELIVERY_IN_PROGRESS)
                .build();

        //then
        assertThrows(OrderServiceException.class,
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
                .status(COMPLETED)
                .createdAt(orderCompleteDatetime)
                .build();
        LocalDateTime canceledDateTime = orderCompleteDatetime.plusDays(1).plusNanos(1);

        //then
        assertThrows(OrderServiceException.class,
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
                .status(COMPLETED)
                .createdAt(now)
                .build();
        LocalDateTime canceledDateTime = now.plusDays(1).minusNanos(1);

        //when
        order.cancel(1L, canceledDateTime);

        //then
        assertEquals(CANCELED, order.getStatus());
    }

    @Test
    void 반품_시_회원_정보가_일치하지_않으면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //then
        assertThrows(OrderServiceException.class,
                //when
                () -> order.returns(2L, null));
    }

    @Test
    void 반품_시_배송_완료_상태가_아니면_예외가_발생한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .status(COMPLETED)
                .build();

        //then
        assertThrows(OrderServiceException.class,
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
                .status(DELIVERED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusDays(1).plusNanos(1);

        //then
        assertThrows(OrderServiceException.class,
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
                .status(DELIVERED)
                .updatedAt(deliveryCompletedDateTime)
                .build();
        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusDays(1).minusNanos(1);

        //when
        order.returns(1L, returnedDateTime);

        //then
        assertEquals(RETURN_IN_PROGRESS, order.getStatus());
    }

    @Test
    void 반품_완료() {
        //given
        Order order = Order.builder()
                .status(RETURN_IN_PROGRESS)
                .build();

        //when
        order.returned();

        //then
        assertEquals(RETURNED, order.getStatus());
    }

    @Test
    void 주문과_회원_정보가_일치하지_않으면_true를_반환한다() {
        //given
        Order order = Order.builder()
                .memberId(2L)
                .build();

        //when
        boolean result = order.isNotOrderBy(1L);

        //then
        assertTrue(result);
    }

    @Test
    void 주문과_회원_정보가_일치하면_false를_반환한다() {
        //given
        Order order = Order.builder()
                .memberId(1L)
                .build();

        //when
        boolean result = order.isNotOrderBy(1L);

        //then
        assertFalse(result);
    }

    @Test
    void 주문_완료() {
        //given
        Order order = Order.builder()
                .status(PENDING)
                .build();

        //when
        order.completed();

        //then
        assertEquals(COMPLETED, order.getStatus());
    }
}