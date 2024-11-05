package com.example.payment.application.feign;


import com.example.payment.dto.OrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-gateway", path = "/orders", contextId = "orderClient")
public interface OrderFeignClient {

    @PostMapping("/internal/{memberId}/validate")
    boolean validateOrderInfo(@PathVariable Long memberId, @RequestBody OrderInfo orderInfo);

}