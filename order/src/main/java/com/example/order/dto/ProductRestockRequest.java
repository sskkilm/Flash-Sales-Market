package com.example.order.dto;

import java.util.List;

public record ProductRestockRequest(
        List<ProductRestockInfo> productRestockInfos
) {
}
