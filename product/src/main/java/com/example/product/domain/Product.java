package com.example.product.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Product {
    private Long id;
    private String name;
    private Money price;
    private int stockQuantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
