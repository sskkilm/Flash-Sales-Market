package com.example.payment.application;

import com.example.payment.dto.PGPaymentRequest;
import com.example.payment.dto.PGPaymentResponse;

public interface PGService {
    PGPaymentResponse requestPayment(PGPaymentRequest request);
}
