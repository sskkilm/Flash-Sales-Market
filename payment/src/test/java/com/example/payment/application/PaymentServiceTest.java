package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.dto.MemberPaymentInfo;
import com.example.payment.dto.OrderValidationRequest;
import com.example.payment.dto.PGInitRequest;
import com.example.payment.dto.PaymentInitRequest;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.external.PGServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

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

}