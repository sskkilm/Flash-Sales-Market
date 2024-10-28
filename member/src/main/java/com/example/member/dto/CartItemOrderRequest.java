package com.example.member.dto;

public record CartItemOrderRequest(
        Long productId,
        int quantity
) {
}
