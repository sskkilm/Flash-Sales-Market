package com.example.order.domain;

import java.time.LocalDateTime;

public class Order {
    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
