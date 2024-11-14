package com.example.product.application.scheduler;

import com.example.product.application.HoldStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HoldingStockScheduler {

    private final HoldStockService holdStockService;

    @Scheduled(fixedDelay = 60000)
    public void releaseInvalidHoldingStock() {
        holdStockService.releaseInvalidHoldingStock();
    }
}
