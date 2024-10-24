package com.example.member.dto;

public record CartItemDto(
        Long cartItemId,
        String productName,
        int quantity
) {

}
