package com.example.payment.application.feign;


import com.example.payment.application.feign.error.decoder.FeignErrorDecoder;
import com.example.payment.dto.OrderValidationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "api-gateway",
        path = "/orders/internal",
        contextId = "orderClient",
        configuration = FeignErrorDecoder.class)
public interface OrderFeignClient {

    @PostMapping("/{memberId}/validate")
    boolean validateOrderInfo(@PathVariable Long memberId, @RequestBody OrderValidationRequest orderValidationRequest);

    @PostMapping("/{orderId}/completed")
    void updateOrderCompleted(@PathVariable Long orderId);

    @PostMapping("/{orderId}}/failed")
    void updateOrderFailed(@PathVariable Long orderId);
}
