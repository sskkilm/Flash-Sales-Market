package com.example.product.exception;

public class InsufficientStockException extends ProductServiceException {

    public InsufficientStockException(String message) {
        super(message);
    }

}
