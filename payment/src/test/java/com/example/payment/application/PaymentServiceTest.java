package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.application.port.PGClient;
import com.example.payment.application.port.EventProducer;
import com.example.payment.application.port.PaymentRepository;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.pg.PGServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.example.payment.domain.PaymentStatus.CONFIRMED;
import static com.example.payment.domain.PaymentStatus.READY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    OrderFeignClient orderFeignClient;

    @Mock
    PGClient pgClient;

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    EventProducer eventProducer;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void 결제_진입_시_이미_승인된_결제이면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, List.of(1L), new BigDecimal("10000"), memberPaymentInfo);
        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(true);
        given(paymentRepository.findOptionalPaymentByOrderId(1L))
                .willReturn(Optional.of(
                                Payment.builder()
                                        .status(CONFIRMED)
                                        .build()
                        )
                );

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입_시_PG사의_서비스에서_예외가_발생하면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, List.of(1L), new BigDecimal("10000"), memberPaymentInfo);

        given(paymentRepository.findOptionalPaymentByOrderId(1L))
                .willReturn(Optional.empty());
        willThrow(PGServiceException.class)
                .given(pgClient)
                .pgInit(any(PGInitRequest.class));

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입_성공() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, List.of(1L), new BigDecimal("10000"), memberPaymentInfo);

        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(true);
        given(paymentRepository.findOptionalPaymentByOrderId(1L))
                .willReturn(Optional.empty());
        given(pgClient.pgInit(any(PGInitRequest.class)))
                .willReturn(new PGInitResponse(
                        "paymentKey", 1L, new BigDecimal(10000), memberPaymentInfo)
                );

        //when
        PaymentInitResponse response = paymentService.init(1L, request);

        //then
        assertEquals("paymentKey", response.paymentKey());
        assertEquals(1L, response.orderId());
        assertEquals(new BigDecimal(10000), response.amount());
        assertEquals(memberPaymentInfo, response.memberPaymentInfo());
    }

    @Test
    void 결제_승인_시_PG사의_서비스에서_예외가_발생하면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, List.of(1L), new BigDecimal("10000"), memberPaymentInfo);

        given(paymentRepository.findByOrderId(1L))
                .willReturn(
                        Payment.builder()
                                .id(1L)
                                .orderId(1L)
                                .amount(new BigDecimal("10000"))
                                .status(READY)
                                .build()
                );
        given(pgClient.pgConfirm(any(PGConfirmRequest.class)))
                .willThrow(PGServiceException.class);

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> paymentService.confirm(
                        "paymentKey", 1L, new BigDecimal("10000"), request
                )
        );
    }

    @Test
    void 결제_승인_성공() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, List.of(1L), new BigDecimal("10000"), memberPaymentInfo);

        given(paymentRepository.findByOrderId(1L))
                .willReturn(
                        Payment.builder()
                                .id(1L)
                                .orderId(1L)
                                .amount(new BigDecimal("10000"))
                                .status(READY)
                                .build()
                );
        given(pgClient.pgConfirm(any(PGConfirmRequest.class)))
                .willReturn(
                        new PGConfirmResponse(1L, new BigDecimal("10000"), "paymentKey")
                );
        //when
        PaymentConfirmResponse response = paymentService.confirm(
                "paymentKey", 1L, new BigDecimal("10000"), request
        );

        //then
        assertEquals(1L, response.paymentId());
        assertEquals(1L, response.orderId());
        assertEquals(new BigDecimal("10000"), response.amount());
        assertEquals("paymentKey", response.paymentKey());
    }

}