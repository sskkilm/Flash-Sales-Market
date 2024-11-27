package com.port90.cartitem.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    CART_ITEM_NOT_FOUND(BAD_REQUEST, "장바구니 항목을 찾을 수 없습니다."),
    MEMBER_INFO_UN_MATCHED(BAD_REQUEST, "사용자 정보가 일치하지 않습니다."),
    CART_IS_EMPTY(BAD_REQUEST, "장바구니가 비어있습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
