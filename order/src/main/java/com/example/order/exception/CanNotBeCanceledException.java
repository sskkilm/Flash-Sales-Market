package com.example.order.exception;

public class CanNotBeCanceledException extends OrderServiceException {

    public CanNotBeCanceledException(String message) {
        super(message);
    }
}
