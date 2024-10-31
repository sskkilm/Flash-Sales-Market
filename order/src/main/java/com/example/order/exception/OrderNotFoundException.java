package com.example.order.exception;

public class OrderNotFoundException extends OrderServiceException {

    public OrderNotFoundException(Long orderId) {
        super("id가 " + orderId + "인 주문을 찾을 수 없습니다.");
    }
}
