package com.example.member.application.feign;

import com.example.member.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-gateway", path = "/products", contextId = "productClient")
public interface ProductFeignClient {

    @GetMapping("/internal/{productId}")
    ProductDto findById(@PathVariable Long productId);
}
