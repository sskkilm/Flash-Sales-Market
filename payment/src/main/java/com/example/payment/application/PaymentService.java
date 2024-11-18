package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.application.port.PGClient;
import com.example.payment.application.port.EventProducer;
import com.example.payment.application.port.PaymentRepository;
import com.example.payment.domain.Payment;
import com.example.payment.domain.event.PaymentConfirmedEvent;
import com.example.payment.domain.event.PaymentFailedEvent;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
import com.example.payment.infrastructure.pg.PGServiceException;
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
    private final PGClient pgClient;
    private final EventProducer eventProducer;

    @Transactional
    public PaymentInitResponse init(Long memberId, PaymentInitRequest request) {

        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                request.orderId(), request.orderProductIds(), request.amount()
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
            pgInitResponse = pgClient.pgInit(
                    new PGInitRequest(
                            request.orderId(), request.amount(), request.memberPaymentInfo()
                    )
            );
        } catch (PGServiceException e) {
            eventProducer.publish(new PaymentFailedEvent(request.orderId()));
            throw new PaymentServiceException(PAYMENT_FAILED_INIT_FAILURE);
        }

        return PaymentInitResponse.from(pgInitResponse);
    }

    @Transactional
    public PaymentConfirmResponse confirm(String paymentKey, Long orderId, BigDecimal amount, PaymentInitRequest paymentInitRequest) {

        Payment payment = paymentRepository.findByOrderId(orderId);
        payment.validate(orderId, amount);

        PGConfirmResponse response = null;
        try {
            response = pgClient.pgConfirm(
                    new PGConfirmRequest(paymentKey, orderId, amount, paymentInitRequest.memberPaymentInfo())
            );
        } catch (PGServiceException e) {
            eventProducer.publish(new PaymentFailedEvent(orderId));
            throw new PaymentServiceException(PAYMENT_FAILED_CONFIRM_FAILURE);
        }

        payment.updatePaymentKey(paymentKey);
        payment.confirmed();
        paymentRepository.save(payment);

        eventProducer.publish(new PaymentConfirmedEvent(orderId));
//        orderFeignClient.updateOrderCompleted(orderId);
//        productFeignClient.applyHoldStock(orderId);

        return new PaymentConfirmResponse(payment.getId(), response.orderId(), response.amount(), response.paymentKey());
    }

}
