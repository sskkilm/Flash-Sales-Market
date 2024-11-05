package com.example.payment.infrastructure;

import com.example.payment.application.PGService;
import com.example.payment.dto.OrderInfo;
import com.example.payment.dto.PGPaymentRequest;
import com.example.payment.dto.PGPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PGServiceImpl implements PGService {

    private final PaymentAuthenticator paymentAuthenticator;

    @Override
    public PGPaymentResponse requestPayment(PGPaymentRequest request) {

        boolean isAuthenticated = paymentAuthenticator.authenticate(request);
        if (!isAuthenticated) {
            return null;
        }

        OrderInfo orderInfo = request.orderInfo();
        String paymentKey = UUID.randomUUID().toString();
        return new PGPaymentResponse(orderInfo.orderId(), orderInfo.totalAmount(), paymentKey);
    }
}
