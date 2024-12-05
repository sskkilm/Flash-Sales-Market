package com.example.payment.application;

import com.example.payment.application.port.PaymentRepository;
import com.example.payment.application.port.feign.OrderClient;
import com.example.payment.application.port.feign.PGClient;
import com.example.payment.common.dto.OrderDto;
import com.example.payment.common.dto.PaymentDto;
import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.request.PaymentConfirmRequest;
import com.example.payment.common.dto.request.PaymentInitRequest;
import com.example.payment.common.dto.response.PGConfirmResponse;
import com.example.payment.domain.Payment;
import com.example.payment.domain.exception.PaymentServiceException;
import com.example.payment.infrastructure.pg.FakePGException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import static com.example.payment.domain.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CacheService cacheService;
    private final OrderClient orderClient;
    private final PGClient pgClient;
    private final PaymentEventPublisher eventPublisher;

    public void init(Long memberId, PaymentInitRequest request) {

        OrderDto order = orderClient.findById(request.orderId());
        validateOrder(memberId, request, order);

        cacheService.saveTemporaryPaymentInfo(order);

        log.info("Payment Init Successes");
    }

    public void confirm(PaymentConfirmRequest request) {

        BigDecimal amount = cacheService.getTemporaryPaymentInfo(request.orderId());
        validateOrderAmount(request, amount);

        PGConfirmResponse response = getPgConfirmResponse(request);
        Payment payment = paymentRepository.save(
                Payment.create(response.orderId(), response.amount(), response.paymentKey())
        );

        eventPublisher.publishPaymentConfirmedEvent(payment);

        log.info("Payment Confirm Successes");
    }

    public List<PaymentDto> getPaymentList(Long memberId) {
        List<Long> orderIds = getOrderIdsByMemberId(memberId);
        return paymentRepository.findByOrderIdInOrderIds(orderIds)
                .stream()
                .map(PaymentDto::from)
                .toList();
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

    private List<Long> getOrderIdsByMemberId(Long memberId) {
        return orderClient.findIdsByMemberId(memberId);
    }

    private static void validateOrderAmount(PaymentConfirmRequest request, BigDecimal amount) {
        if (amount.compareTo(request.amount()) != 0) {
            throw new PaymentServiceException(INVALID_ORDER_AMOUNT);
        }
    }

    private PGConfirmResponse getPgConfirmResponse(PaymentConfirmRequest request) {
        PGConfirmResponse response = null;
        try {
            response = pgClient.pgConfirm(
                    new PGConfirmRequest(request.paymentKey(), request.orderId(), request.amount()), request.flag()
            );
        } catch (FakePGException e) {

            eventPublisher.publishPaymentFailedEvent(request.orderId());

            log.info("Payment Confirm Fail");

            throw new PaymentServiceException(PAYMENT_CONFIRM_FAILED, e);
        } finally {
            cacheService.deleteTemporaryPaymentInfo(request.orderId());
        }
        return response;
    }
}
