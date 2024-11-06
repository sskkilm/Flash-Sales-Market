package com.example.payment.domain;

import com.example.payment.exception.PaymentServiceException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.example.payment.domain.PaymentStatus.CONFIRMED;
import static com.example.payment.domain.PaymentStatus.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    @Test
    void 결제_생성() {
        //given

        //when
        Payment payment = Payment.create(1L, new BigDecimal("10000"));

        //then
        assertEquals(1L, payment.getOrderId());
        assertEquals(new BigDecimal("10000"), payment.getAmount());
        assertEquals(READY, payment.getStatus());
    }

    @Test
    void 결제_검증_시_주문_ID가_일치하지_않으면_예외가_발생한다() {
        //given
        Payment payment = Payment.builder()
                .orderId(1L)
                .amount(new BigDecimal("10000"))
                .paymentKey("paymentKey")
                .build();

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> payment.validate(2L, new BigDecimal("10000"))
        );
    }

    @Test
    void 결제_검증_시_주문_금액이_일치하지_않으면_예외가_발생한다() {
        //given
        Payment payment = Payment.builder()
                .orderId(1L)
                .amount(new BigDecimal("10000"))
                .paymentKey("paymentKey")
                .build();

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> payment.validate(1L, new BigDecimal("15000"))
        );
    }

    @Test
    void 결제_승인() {
        //given
        Payment payment = Payment.builder()
                .status(READY)
                .build();

        //when
        payment.confirmed("paymentKey");

        //then
        assertEquals("paymentKey", payment.getPaymentKey());
        assertEquals(CONFIRMED, payment.getStatus());
    }
}