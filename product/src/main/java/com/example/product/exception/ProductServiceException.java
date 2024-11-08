package com.example.product.exception;

import com.example.product.exception.error.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductServiceException extends RuntimeException {
    private final ErrorCode code;
}
