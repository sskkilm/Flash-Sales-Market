package com.example.order.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    PENDING_PAYMENT("결제 대기"),
    PAYMENT_CONFIRMED("결제 완료"),
    CANCELED("주문 취소"),
    ;

    private final String message;
}
