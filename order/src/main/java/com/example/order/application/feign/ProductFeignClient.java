package com.example.order.application.feign;

import com.example.order.dto.ProductPurchaseRequest;
import com.example.order.dto.ProductPurchaseResponse;
import com.example.order.dto.ProductRestoreStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-gateway", path = "/products", contextId = "productClient")
public interface ProductFeignClient {

    @PostMapping("/internal/purchase")
    ProductPurchaseResponse purchase(ProductPurchaseRequest productPurchaseRequest);

    @PostMapping("/internal/restore-stock")
    void restoreStock(ProductRestoreStockRequest productRestoreStockRequest);

}
