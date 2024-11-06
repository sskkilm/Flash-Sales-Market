package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.external.PGServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.payment.exception.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final PGService pgService;

    @Transactional
    public void init(Long memberId, PaymentInitRequest request) {

        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                request.orderId(), request.amount()
        );
        if (invalidOrderInfo(memberId, orderValidationRequest)) {
            throw new PaymentServiceException(INVALID_ORDER_INFO);
        }
        if (paymentRepository.existsByOrderId(request.orderId())) {
            throw new PaymentServiceException(PAYMENT_INFO_ALREADY_EXIST);
        }

        // 결제 데이터 임시 저장
        paymentRepository.save(
                Payment.create(request.orderId(), request.amount())
        );

        try {
            pgService.init(
                    new PGInitRequest(
                            request.orderId(), request.amount(), request.memberPaymentInfo()
                    )
            );
        } catch (PGServiceException e) {
            throw new PaymentServiceException(PAYMENT_FAILED_AUTHENTICATION_FAILURE);
        }

    }

    public PaymentConfirmResponse confirm(String paymentKey, Long orderId, BigDecimal amount, MemberPaymentInfo memberPaymentInfo) {

        Payment payment = paymentRepository.findByOrderId(orderId);
        payment.validate(orderId, amount);

        PGConfirmResponse response = null;
        try {
            response = pgService.confirm(
                    new PGConfirmRequest(paymentKey, orderId, amount, memberPaymentInfo)
            );
        } catch (PGServiceException e) {
            throw new PaymentServiceException(PAYMENT_FAILED_CONFIRM_FAILURE);
        }

        payment.confirmed(response.paymentKey());
        paymentRepository.save(payment);

        // TODO: 주문 상태 완료로 변경

        return new PaymentConfirmResponse(payment.getId(), response.orderId(), response.amount(), response.paymentKey());
    }

    private boolean invalidOrderInfo(Long memberId, OrderValidationRequest orderValidationRequest) {
        return !orderFeignClient.validateOrderInfo(memberId, orderValidationRequest);
    }

}
