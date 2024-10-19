package com.example.product.domain;

import java.time.LocalDateTime;

public class Product {
    private Long id;
    private Money price;
    private int stockQuantity;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
