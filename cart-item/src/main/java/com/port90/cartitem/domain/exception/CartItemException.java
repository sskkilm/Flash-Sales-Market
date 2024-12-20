package com.port90.cartitem.domain.exception;

import lombok.Getter;

@Getter
public class CartItemException extends RuntimeException {

    private final ErrorCode errorCode;

    public CartItemException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public CartItemException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
