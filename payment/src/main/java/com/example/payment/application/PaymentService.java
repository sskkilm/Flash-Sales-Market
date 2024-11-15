package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.application.feign.ProductFeignClient;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.event.PaymentConfirmedEvent;
import com.example.payment.event.PaymentFailedEvent;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.external.PGServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.payment.exception.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final ProductFeignClient productFeignClient;
    private final PGService pgService;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public PaymentInitResponse init(Long memberId, PaymentInitRequest request) {

        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                request.orderId(), request.amount()
        );
        orderFeignClient.validateOrderInfo(memberId, orderValidationRequest);

        Payment payment = paymentRepository.findOptionalPaymentByOrderId(request.orderId())
                .map(p -> {
                    if (p.isConfirmed()) {
                        throw new PaymentServiceException(PAYMENT_ALREADY_CONFIRMED);
                    }
                    return p;
                })
                .orElseGet(
                        () -> Payment.create(request.orderId(), request.amount())
                );
        paymentRepository.save(payment);

        PGInitResponse pgInitResponse = null;
        try {
            pgInitResponse = pgService.pgInit(
                    new PGInitRequest(
                            request.orderId(), request.amount(), request.memberPaymentInfo()
                    )
            );
        } catch (PGServiceException e) {
            eventPublisher.publishEvent(new PaymentFailedEvent(request.orderId()));
            throw new PaymentServiceException(PAYMENT_FAILED_INIT_FAILURE);
        }

        return PaymentInitResponse.from(pgInitResponse);
    }

    @Transactional
    public PaymentConfirmResponse confirm(String paymentKey, Long orderId, BigDecimal amount, MemberPaymentInfo memberPaymentInfo) {

        Payment payment = paymentRepository.findByOrderId(orderId);
        payment.validate(orderId, amount);

        PGConfirmResponse response = null;
        try {
            response = pgService.pgConfirm(
                    new PGConfirmRequest(paymentKey, orderId, amount, memberPaymentInfo)
            );
        } catch (PGServiceException e) {
            eventPublisher.publishEvent(new PaymentFailedEvent(orderId));
            throw new PaymentServiceException(PAYMENT_FAILED_CONFIRM_FAILURE);
        }

        payment.updatePaymentKey(paymentKey);
        payment.confirmed();
        paymentRepository.save(payment);

        eventPublisher.publishEvent(new PaymentConfirmedEvent(orderId));
//        orderFeignClient.updateOrderCompleted(orderId);
//        productFeignClient.applyHoldStock(orderId);

        return new PaymentConfirmResponse(payment.getId(), response.orderId(), response.amount(), response.paymentKey());
    }

}
