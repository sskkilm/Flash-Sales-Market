package com.port90.cartitem.common.dto;

public record CartItemDto(
        Long cartItemId,
        String productName,
        int quantity
) {

}
