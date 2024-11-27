package com.port90.cartitem.common.dto.request;

import jakarta.validation.constraints.Min;

public record CartItemUpdateRequest(
        @Min(1) int quantity
) {

}
