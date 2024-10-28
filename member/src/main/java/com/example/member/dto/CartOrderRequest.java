package com.example.member.dto;

import java.util.List;

public record CartOrderRequest(
        List<CartItemOrderRequest> orderProducts
) {
}
