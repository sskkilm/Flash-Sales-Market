package com.example.member.dto;

import jakarta.validation.constraints.Min;

public record CartItemUpdateRequest(
        @Min(1) int quantity
) {

}
