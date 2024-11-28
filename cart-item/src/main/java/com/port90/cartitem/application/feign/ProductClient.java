package com.port90.cartitem.application.feign;

import com.port90.cartitem.common.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-gateway",
        path = "/products/internal",
        contextId = "productClient")
public interface ProductClient {

    @GetMapping("/{productId}")
    ProductDto getProductInfo(@PathVariable Long productId);
}
