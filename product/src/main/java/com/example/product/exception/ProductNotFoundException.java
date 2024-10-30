package com.example.product.exception;

public class ProductNotFoundException extends ProductServiceException {

    public ProductNotFoundException(Long productId) {
        super("id가 " + productId + "인 상품을 찾을 수 없습니다.");
    }

}
