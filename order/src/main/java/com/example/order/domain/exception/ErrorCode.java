package com.example.order.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 주문입니다."),
    INSUFFICIENT_STOCK(BAD_REQUEST, "상품 재고가 부족합니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
