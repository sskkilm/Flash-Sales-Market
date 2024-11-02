package com.example.order.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    IN_PROGRESS("주문 진행중"),
    WAITING_FOR_PAYMENT("결제 대기중"),
    COMPLETED("주문 완료"),
    CANCELED("주문 취소"),
    DELIVERY_IN_PROGRESS("배송 진행중"),
    DELIVERED("배송 완료"),
    RETURN_IN_PROGRESS("반품 진행중"),
    RETURNED("반품 완료"),
    ;

    private final String message;
}
