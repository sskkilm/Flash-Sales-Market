package com.example.payment.application.feign;


import com.example.payment.application.feign.error.decoder.FeignErrorDecoder;
import com.example.payment.common.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-gateway",
        path = "/orders/internal",
        contextId = "orderClient",
        configuration = FeignErrorDecoder.class)
public interface OrderFeignClient {

    @GetMapping("/{orderId}")
    OrderDto getOrder(@PathVariable Long orderId);
}
