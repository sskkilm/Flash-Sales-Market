package com.example.payment.domain.exception;

import lombok.Getter;

@Getter
public class PaymentServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public PaymentServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public PaymentServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
