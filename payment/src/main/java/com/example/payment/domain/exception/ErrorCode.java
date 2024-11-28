package com.example.payment.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ORDER_AMOUNT(BAD_REQUEST, "주문 금액이 유효하지 않습니다."),
    ORDER_ALREADY_PROCESSED(BAD_REQUEST, "이미 처리된 주문입니다."),
    ORDER_MEMBER_UNMATCHED(BAD_REQUEST, "주문 회원 정보가 일치하지 않습니다."),

    PAYMENT_NOT_FOUND(BAD_REQUEST, "결제 정보가 존재하지 않습니다."),
    PAYMENT_INIT_FAILED(BAD_REQUEST, "결제 요청에 실패했습니다."),
    PAYMENT_CONFIRM_FAILED(BAD_REQUEST, "결제 승인에 실패했습니다."),
    PAYMENT_ALREADY_PROCESSED(BAD_REQUEST, "이미 처리된 결제입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;
    private final HttpStatus status;
    private final String message;
}
