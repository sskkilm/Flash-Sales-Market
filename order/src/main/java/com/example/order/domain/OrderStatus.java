package com.example.order.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    ORDER_COMPLETED("주문 완료"),
    DELIVERY_IN_PROGRESS("배송 진행중"),
    DELIVERY_COMPLETED("배송 완료"),
    RETURN_IN_PROGRESS("반품 진행중"),
    RETURN_COMPLETED("반품 완료"),
    ;

    private final String message;
}
