package com.example.payment.application.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-gateway", path = "/products/internal", contextId = "productClient")
public interface ProductFeignClient {

    @PostMapping("/{orderId}/release/holding-stock")
    void releaseHoldingStock(@PathVariable Long orderId);

    @PostMapping("/{orderId}/apply/hold-stock")
    void applyHoldStock(@PathVariable Long orderId);
}
