package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.external.PGServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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
    PGService pgService;

    @Mock
    PaymentRepository paymentRepository;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void 결제_진입_시_주문_정보가_유효하지_않으면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, new BigDecimal("10000"), memberPaymentInfo);

        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(false);

        //then
        assertThrows(PaymentServiceException.class,
                // when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입_시_이미_결제_정보가_존재하면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, new BigDecimal("10000"), memberPaymentInfo);

        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(true);
        given(paymentRepository.existsByOrderId(1L))
                .willReturn(true);

        //then
        assertThrows(PaymentServiceException.class,
                // when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입_시_PG사의_서비스에서_예외가_발생하면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        PaymentInitRequest request = new PaymentInitRequest(
                1L, new BigDecimal("10000"), memberPaymentInfo);

        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(true);
        given(paymentRepository.existsByOrderId(1L))
                .willReturn(false);
        willThrow(PGServiceException.class)
                .given(pgService)
                .init(any(PGInitRequest.class));

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
                1L, new BigDecimal("10000"), memberPaymentInfo);

        given(orderFeignClient.validateOrderInfo(eq(1L), any(OrderValidationRequest.class)))
                .willReturn(true);
        given(paymentRepository.existsByOrderId(1L))
                .willReturn(false);

        //when
        paymentService.init(1L, request);

        //then
    }

    @Test
    void 결제_승인_시_주문_ID에_대응하는_결제_정보가_없으면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        given(paymentRepository.findByOrderId(1L))
                .willThrow(PaymentServiceException.class);

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> paymentService.confirm(
                        "paymentKey", 1L, new BigDecimal("10000"), memberPaymentInfo
                )
        );
    }

    @Test
    void 결제_승인_시_PG사의_서비스에서_예외가_발생하면_예외가_발생한다() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        given(paymentRepository.findByOrderId(1L))
                .willReturn(
                        Payment.builder()
                                .id(1L)
                                .orderId(1L)
                                .amount(new BigDecimal("10000"))
                                .status(READY)
                                .build()
                );
        given(pgService.confirm(any(PGConfirmRequest.class)))
                .willThrow(PGServiceException.class);

        //then
        assertThrows(PaymentServiceException.class,
                //when
                () -> paymentService.confirm(
                        "paymentKey", 1L, new BigDecimal("10000"), memberPaymentInfo
                )
        );
    }

    @Test
    void 결제_승인_성공() {
        //given
        MemberPaymentInfo memberPaymentInfo = new MemberPaymentInfo(true, true);
        given(paymentRepository.findByOrderId(1L))
                .willReturn(
                        Payment.builder()
                                .id(1L)
                                .orderId(1L)
                                .amount(new BigDecimal("10000"))
                                .status(READY)
                                .build()
                );
        given(pgService.confirm(any(PGConfirmRequest.class)))
                .willReturn(
                        new PGConfirmResponse(1L, new BigDecimal("10000"), "paymentKey")
                );
        //when
        PaymentConfirmResponse response = paymentService.confirm(
                "paymentKey", 1L, new BigDecimal("10000"), memberPaymentInfo
        );

        //then
        assertEquals(1L, response.paymentId());
        assertEquals(1L, response.orderId());
        assertEquals(new BigDecimal("10000"), response.amount());
        assertEquals("paymentKey", response.paymentKey());
    }

}