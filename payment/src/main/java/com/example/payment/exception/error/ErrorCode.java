package com.example.payment.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ORDER_INFO("유효하지 않은 주문 정보입니다."),
    PAYMENT_INFO_ALREADY_EXIST("결제 정보가 이미 존재합니다."),
    PAYMENT_INFO_DOES_NOT_EXIST("결제 정보가 존재하지 않습니다."),
    PAYMENT_ALREADY_CONFIRMED("이미 승인된 결제입니다."),
    ORDER_ID_DOES_NOT_MATCH("주문 ID가 일치하지 않습니다."),
    ORDER_AMOUNT_DOES_NOT_MATCH("주문 금액이 일치하지 않습니다."),
    PAYMENT_FAILED_AUTHENTICATION_FAILURE("인증 실패로 인해 결제에 실패했습니다."),
    PAYMENT_FAILED_CONFIRM_FAILURE("승인 실패로 인해 결제에 실패했습니다.");

    private final String message;
}
