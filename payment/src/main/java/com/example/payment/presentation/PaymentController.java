package com.example.payment.presentation;

import com.example.payment.application.PaymentService;
import com.example.payment.dto.PaymentInitRequest;
import com.example.payment.dto.PaymentInitResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentInitResponse init(Long memberId, PaymentInitRequest request) {
        return paymentService.init(memberId, request);
    }
}
