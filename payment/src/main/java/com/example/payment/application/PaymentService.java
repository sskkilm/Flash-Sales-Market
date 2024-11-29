package com.example.payment.application;

import com.example.payment.application.port.feign.OrderClient;
import com.example.payment.application.port.feign.PGClient;
import com.example.payment.application.port.PaymentRepository;
import com.example.payment.common.dto.OrderDto;
import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.request.PaymentConfirmRequest;
import com.example.payment.common.dto.request.PaymentInitRequest;
import com.example.payment.common.dto.response.PGConfirmResponse;
import com.example.payment.domain.Payment;
import com.example.payment.domain.exception.PaymentServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import static com.example.payment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final PGClient pgClient;

    @Transactional
    public void init(Long memberId, PaymentInitRequest request) {

        OrderDto order = orderClient.findById(request.orderId());
        validateOrder(memberId, request, order);

        paymentRepository.save(Payment.create(request.orderId(), request.amount()));

        log.info("Payment Init Successes");
    }

    @Transactional
    public void confirm(PaymentConfirmRequest request) {

        Payment payment = paymentRepository.findByOrderId(request.orderId());
        payment.validate(request.amount());

        PGConfirmResponse response = null;
        try {
            response = pgClient.pgConfirm(
                    new PGConfirmRequest(request.paymentKey(), request.orderId(), request.amount()), request.flag()
            );
        } catch (Exception e) {
            paymentRepository.rollBack(payment);

            orderClient.paymentFailed(request.orderId());

            log.info("Payment Confirm Fail");
            throw new PaymentServiceException(PAYMENT_CONFIRM_FAILED, e);
        }

        payment.updatePaymentKey(response.paymentKey());
        payment.confirmed();
        paymentRepository.save(payment);

        orderClient.paymentConfirmed(response.orderId());

        log.info("Payment Confirm Successes");
    }

    private static void validateOrder(Long memberId, PaymentInitRequest request, OrderDto order) {
        if (!Objects.equals(order.memberId(), memberId)) {
            throw new PaymentServiceException(ORDER_MEMBER_UNMATCHED);
        }
        if (!"PENDING_PAYMENT".equals(order.status())) {
            throw new PaymentServiceException(ORDER_ALREADY_PROCESSED);
        }
        if (request.amount().compareTo(order.amount()) != 0) {
            throw new PaymentServiceException(INVALID_ORDER_AMOUNT);
        }
    }
}
