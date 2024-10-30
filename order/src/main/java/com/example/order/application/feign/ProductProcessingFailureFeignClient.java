package com.example.order.application.feign;

import com.example.order.application.feign.fallback.ProductProcessingFailureFallBack;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Retry(name = "default")
@CircuitBreaker(name = "default")
@FeignClient(name = "api-gateway", path = "/products", fallback = ProductProcessingFailureFallBack.class)
public interface ProductProcessingFailureFeignClient {

    @GetMapping("/disability-situation/case1")
    ResponseEntity<String> case1();

    @GetMapping("/disability-situation/case2")
    ResponseEntity<String> case2();

    @GetMapping("/disability-situation/case3")
    ResponseEntity<String> case3();

}
