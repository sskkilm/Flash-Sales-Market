package com.example.payment.exception;

import com.example.payment.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class PaymentServiceException extends RuntimeException {
    private final HttpStatus status;
    private final String code;
    private final String message;

    public PaymentServiceException(ErrorCode errorCode) {
        this.status = errorCode.getStatus();
        this.code = errorCode.name();
        this.message = errorCode.getMessage();
    }
}
