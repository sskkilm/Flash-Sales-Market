package com.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제 준비 완료");

    private final String message;
}
