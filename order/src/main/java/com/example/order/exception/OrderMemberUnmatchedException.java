package com.example.order.exception;

public class OrderMemberUnmatchedException extends OrderServiceException {

    public OrderMemberUnmatchedException(String message) {
        super(message);
    }
}
