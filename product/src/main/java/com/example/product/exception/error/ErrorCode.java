package com.example.product.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INSUFFICIENT_STOCK(BAD_REQUEST, "상품 재고가 부족합니다."),
    CONTAINS_MISSING_PRODUCT_INFORMATION(BAD_REQUEST, "없는 상품 정보가 포함되어 있습니다."),
    PRODUCT_NOT_FOUND(BAD_REQUEST, "존재하지 않는 상품입니다."),
    NOT_OPENED(BAD_REQUEST, "오픈되지 않은 이벤트 상품입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
