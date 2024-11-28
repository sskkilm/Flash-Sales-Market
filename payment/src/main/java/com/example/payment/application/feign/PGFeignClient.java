package com.example.payment.application.feign;

import com.example.payment.common.dto.request.PGConfirmRequest;
import com.example.payment.common.dto.request.PGInitRequest;
import com.example.payment.common.dto.response.PGConfirmResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "pg-service", url = "http://localhost:8087/pg")
public interface PGFeignClient {

    @PostMapping("/init")
    void pgInit(PGInitRequest pgInitRequest);

    @PostMapping("/confirm")
    PGConfirmResponse pgConfirm(PGConfirmRequest request);

}
