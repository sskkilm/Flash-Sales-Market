package com.example.payment.infrastructure.pg;

import lombok.Getter;

@Getter
public class FakePGException extends RuntimeException {

    private final String message;

    public FakePGException(String message) {
        super(message);
        this.message = message;
    }
}
