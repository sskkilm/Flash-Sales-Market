package com.example.order.exception;

public class CanNotBeReturnedException extends OrderServiceException {

    public CanNotBeReturnedException(String message) {
        super(message);
    }
}
