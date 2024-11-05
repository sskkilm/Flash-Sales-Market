package com.example.payment.application;

import com.example.payment.application.feign.OrderFeignClient;
import com.example.payment.domain.Payment;
import com.example.payment.dto.*;
import com.example.payment.exception.PaymentServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.payment.exception.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderFeignClient orderFeignClient;
    private final PGService pgService;

    @Transactional
    public PaymentInitResponse init(Long memberId, PaymentInitRequest request) {

        // 주문 정보 검증
        OrderInfo orderInfo = request.orderInfo();
        if (paymentRepository.existsByOrderId(orderInfo.orderId())) {
            throw new PaymentServiceException(PAYMENT_INFO_ALREADY_EXIST);
        }

        if (invalidOrderInfo(memberId, orderInfo)) {
            throw new PaymentServiceException(INVALID_ORDER_INFO);
        }

        PaymentInfo paymentInfo = request.paymentInfo();
        PGPaymentResponse response = pgService.requestPayment(
                new PGPaymentRequest(orderInfo, paymentInfo)
        );
        // 결제 시도가 실패한 경우 예외 발생
        if (response == null) {
            throw new PaymentServiceException(PAYMENT_FAILED);
        }

        // 결제 데이터 임시 저장
        Payment payment = paymentRepository.save(
                Payment.create(orderInfo.orderId(), orderInfo.totalAmount())
        );

        return new PaymentInitResponse(
                payment.getId(), payment.getOrderId(), payment.getAmount(), response.paymentKey()
        );
    }

    private boolean invalidOrderInfo(Long memberId, OrderInfo orderInfo) {
        return !orderFeignClient.validateOrderInfo(memberId, orderInfo);
    }

}
