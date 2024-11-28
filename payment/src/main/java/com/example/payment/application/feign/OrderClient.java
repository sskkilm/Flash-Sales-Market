package com.example.payment.application.feign;


import com.example.payment.application.feign.error.decoder.FeignErrorDecoder;
import com.example.payment.common.dto.OrderDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-gateway",
        path = "/orders/internal",
        contextId = "orderClient",
        configuration = FeignErrorDecoder.class)
public interface OrderClient {

    @GetMapping("/{orderId}")
    OrderDto findById(@PathVariable Long orderId);

    @PostMapping("/{orderId}/payment/fail")
    void paymentFailed(@PathVariable Long orderId);

    @PostMapping("/{orderId}/payment/confirmed")
    void paymentConfirmed(@PathVariable Long orderId);
}
