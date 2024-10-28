package com.example.member.application.feign;

import com.example.member.dto.CartOrderRequest;
import com.example.member.dto.CartOrderResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-gateway", path = "/orders", contextId = "orderClient")
public interface OrderFeignClient {

    @PostMapping("/{memberId}")
    CartOrderResponse create(
            @PathVariable Long memberId,
            @RequestBody CartOrderRequest cartOrderRequest
    );
}
