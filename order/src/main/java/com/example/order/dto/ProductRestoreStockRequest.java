package com.example.order.dto;

import java.util.List;

public record ProductRestoreStockRequest(
        List<ProductRestoreStockInfo> productRestoreStockInfos
) {
}
