package com.example.payment.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    INVALID_ORDER_INFO(BAD_REQUEST, "유효하지 않은 주문 정보입니다."),
    PAYMENT_INFO_DOES_NOT_EXIST(BAD_REQUEST, "결제 정보가 존재하지 않습니다."),
    PAYMENT_ALREADY_CONFIRMED(BAD_REQUEST, "이미 승인된 결제입니다."),
    ORDER_ID_DOES_NOT_MATCH(BAD_REQUEST, "주문 ID가 일치하지 않습니다."),
    ORDER_AMOUNT_DOES_NOT_MATCH(BAD_REQUEST, "주문 금액이 일치하지 않습니다."),
    PAYMENT_FAILED_INIT_FAILURE(BAD_REQUEST, "결제 진입에 실패했습니다."),
    PAYMENT_FAILED_CONFIRM_FAILURE(BAD_REQUEST, "결제 승인에 실패했습니다."),

    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 주문입니다."),
    MEMBER_UN_MATCHED(HttpStatus.BAD_REQUEST, "회원 정보가 일치하지 않습니다"),
    TOTAL_AMOUNT_MIS_MATCH(HttpStatus.BAD_REQUEST, "총 주문 금액이 맞지 않습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;
    private final HttpStatus status;
    private final String message;
}
