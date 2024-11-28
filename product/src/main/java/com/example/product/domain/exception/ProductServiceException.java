package com.example.product.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductServiceException extends RuntimeException {
    private final ErrorCode code;
}
