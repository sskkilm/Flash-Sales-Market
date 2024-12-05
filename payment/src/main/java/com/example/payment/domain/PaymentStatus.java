package com.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    CONFIRMED("승인");

    private final String message;
}
