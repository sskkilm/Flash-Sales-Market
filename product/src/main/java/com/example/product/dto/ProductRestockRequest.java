package com.example.product.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProductRestockRequest(
        @NotEmpty List<ProductRestockInfo> productRestockInfos
) {
}
