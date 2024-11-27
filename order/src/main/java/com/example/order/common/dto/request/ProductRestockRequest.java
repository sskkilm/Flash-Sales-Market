package com.example.order.common.dto.request;

import com.example.order.common.dto.ProductRestockInfo;

import java.util.List;

public record ProductRestockRequest(
        List<ProductRestockInfo> productRestockInfos
) {
}
