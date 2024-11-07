package com.example.order.exception;

import com.example.order.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderServiceException extends RuntimeException {
    private final ErrorCode errorCode;
}
