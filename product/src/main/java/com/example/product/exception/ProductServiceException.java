package com.example.product.exception;

public abstract class ProductServiceException extends RuntimeException {

    public ProductServiceException(String message) {
        super(message);
    }
}
