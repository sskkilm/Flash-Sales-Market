package com.example.payment.exception.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ORDER_INFO("유효하지 않은 주문 정보입니다."),
    PAYMENT_FAILED("결제 실패")
    ;

    private final String message;
}
