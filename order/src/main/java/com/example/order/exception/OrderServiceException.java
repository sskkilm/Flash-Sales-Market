package com.example.order.exception;

public abstract class OrderServiceException extends RuntimeException {

    public OrderServiceException(String message) {
        super(message);
    }
}
