package com.example.product.application.feign;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "api-gateway", path = "/orders", contextId = "orderClient")
public interface OrderFeignClient {


}
