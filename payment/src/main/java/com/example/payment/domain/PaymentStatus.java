package com.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    READY("결제 준비 완료"),
    CONFIRMED("결제 승인");

    private final String message;
}
