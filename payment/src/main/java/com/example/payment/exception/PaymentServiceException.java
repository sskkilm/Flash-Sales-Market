package com.example.payment.exception;

import com.example.payment.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
