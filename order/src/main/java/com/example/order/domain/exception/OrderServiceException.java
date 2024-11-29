package com.example.order.domain.exception;

import lombok.Getter;

@Getter
public class OrderServiceException extends RuntimeException {
    private final ErrorCode errorCode;

    public OrderServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public OrderServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
