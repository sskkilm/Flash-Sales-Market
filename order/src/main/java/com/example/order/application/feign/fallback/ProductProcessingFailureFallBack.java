package com.example.order.application.feign.fallback;

import com.example.order.application.feign.ProductProcessingFailureFeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductProcessingFailureFallBack implements ProductProcessingFailureFeignClient {
    @Override
    public ResponseEntity<String> case1() {
        return ResponseEntity.status(500).body("서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }

    @Override
    public ResponseEntity<String> case2() {
        return ResponseEntity.status(503).body("서비스가 현재 사용 불가능합니다. 잠시 후 다시 시도해주세요.");
    }

    @Override
    public ResponseEntity<String> case3() {
        return ResponseEntity.status(500).body("서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
    }
}
