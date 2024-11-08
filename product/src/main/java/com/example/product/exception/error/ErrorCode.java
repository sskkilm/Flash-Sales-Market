package com.example.product.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
    PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 상품입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
