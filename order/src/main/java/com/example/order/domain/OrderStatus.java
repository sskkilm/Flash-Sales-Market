package com.example.order.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_PAYMENT("결제 대기"),
    PAYMENT_FAILED("결제 실패"),
    PAYMENT_CONFIRMED("결제 승인"),
    ;

    private final String message;
}
