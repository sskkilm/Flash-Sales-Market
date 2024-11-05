package com.example.payment.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ORDER_INFO("유효하지 않은 주문 정보입니다."),
    PAYMENT_INFO_ALREADY_EXIST("결제 정보가 이미 존재합니다,"),
    PAYMENT_FAILED("결제에 실패했습니다");

    private final String message;
}
