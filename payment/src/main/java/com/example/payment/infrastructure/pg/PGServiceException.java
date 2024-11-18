package com.example.payment.infrastructure.pg;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PGServiceException extends RuntimeException {
    private final String message;
}
