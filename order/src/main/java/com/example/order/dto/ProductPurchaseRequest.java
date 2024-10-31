package com.example.order.dto;

import java.util.List;

public record ProductPurchaseRequest(
        List<ProductPurchaseInfo> productPurchaseInfos
) {
}
