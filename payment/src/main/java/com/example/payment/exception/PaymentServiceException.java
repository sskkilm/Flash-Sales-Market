package com.example.payment.exception;

import com.example.payment.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PaymentServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
