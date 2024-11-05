package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
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
import static org.mockito.BDDMockito.given;

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
        OrderInfo orderInfo = new OrderInfo(1L, new BigDecimal("10000"));
        PaymentInfo paymentInfo = new PaymentInfo(true);
        PaymentInitRequest request = new PaymentInitRequest(orderInfo, paymentInfo);

        given(orderFeignClient.validateOrderInfo(1L, orderInfo))
                .willReturn(false);

        //then
        assertThrows(PaymentServiceException.class,
                // when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입_시_결제_시도가_실패한_경우_예외가_발생한다() {
        //given
        OrderInfo orderInfo = new OrderInfo(1L, new BigDecimal("10000"));
        PaymentInfo paymentInfo = new PaymentInfo(true);
        PaymentInitRequest request = new PaymentInitRequest(orderInfo, paymentInfo);

        given(orderFeignClient.validateOrderInfo(1L, orderInfo))
                .willReturn(true);
        given(pgService.requestPayment(any(PGPaymentRequest.class)))
                .willReturn(null);

        //then
        assertThrows(PaymentServiceException.class,
                // when
                () -> paymentService.init(1L, request));
    }

    @Test
    void 결제_진입이_성공한다() {
        //given
        OrderInfo orderInfo = new OrderInfo(1L, new BigDecimal("10000"));
        PaymentInfo paymentInfo = new PaymentInfo(true);
        PaymentInitRequest request = new PaymentInitRequest(orderInfo, paymentInfo);

        given(orderFeignClient.validateOrderInfo(1L, orderInfo))
                .willReturn(true);
        String paymentKey = "paymentKey";
        given(pgService.requestPayment(any(PGPaymentRequest.class)))
                .willReturn(
                        new PGPaymentResponse(1L, new BigDecimal("10000"), paymentKey)
                );

        given(paymentRepository.save(any(Payment.class)))
                .willReturn(
                        Payment.builder()
                                .id(1L)
                                .orderId(1L)
                                .amount(new BigDecimal("10000"))
                                .status(READY)
                                .build()
                );

        //when
        PaymentInitResponse response = paymentService.init(1L, request);

        //then
        assertEquals(1L, response.paymentId());
        assertEquals(1L, response.orderId());
        assertEquals(new BigDecimal("10000"), response.totalAmount());
        assertEquals(paymentKey, response.paymentKey());
    }

}