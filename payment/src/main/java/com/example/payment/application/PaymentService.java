package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.application.feign.PGFeignClient;
import com.example.payment.application.port.EventProducer;
import com.example.payment.application.port.PaymentRepository;
import com.example.payment.common.dto.OrderDto;
import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.request.PGInitRequest;
import com.example.payment.common.dto.request.PaymentInitRequest;
import com.example.payment.domain.Payment;
import com.example.payment.domain.exception.PaymentServiceException;
import com.example.payment.infrastructure.pg.PGServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.example.payment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final PGFeignClient pgFeignClient;
    private final EventProducer eventProducer;

    @Transactional
    public void init(Long memberId, PaymentInitRequest request) {

        OrderDto order = orderFeignClient.getOrder(request.orderId());
        if (!"PENDING_PAYMENT".equals(order.status())) {
            throw new PaymentServiceException(ORDER_ALREADY_PROCESSED);
        }
        if (request.amount().compareTo(order.amount()) != 0) {
            throw new PaymentServiceException(INVALID_ORDER_AMOUNT);
        }

        paymentRepository.save(Payment.create(request.orderId(), request.amount()));
        try {
            pgFeignClient.pgInit(
                    new PGInitRequest(request.orderId(), request.amount())
            );
        } catch (Exception e) {
            log.info("Payment Init Fail");
            throw new PaymentServiceException(PAYMENT_INIT_FAILED, e);
        }

        log.info("Payment Init Successes");
    }

    @Transactional
    public void confirm(String paymentKey, Long orderId, BigDecimal amount) {

        Payment payment = paymentRepository.findByOrderId(orderId);
        payment.validate(amount);

        try {
            pgFeignClient.pgConfirm(
                    new PGConfirmRequest(paymentKey, orderId, amount)
            );
        } catch (PGServiceException e) {
            if (payment.isPending()) {
                payment.failed();
                paymentRepository.save(payment);
            }
            log.info("Payment Confirm Fail");
            throw new PaymentServiceException(PAYMENT_CONFIRM_FAILED);
        }

        payment.updatePaymentKey(paymentKey);
        payment.confirmed();
        paymentRepository.save(payment);

        log.info("Payment Confirm Successes");
    }

    private static boolean orderAmountUnmatched(PaymentInitRequest request, BigDecimal amount) {
        return amount.compareTo(request.amount()) != 0;
    }
}
