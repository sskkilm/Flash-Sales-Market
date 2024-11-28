package com.example.payment.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PaymentStatus {
    PENDING("대기"),
    FAILED("실패"),
    CONFIRMED("승인");

    private final String message;
}
