package com.example.product.application.scheduler;

import com.example.product.application.HoldingStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HoldingStockScheduler {

    private final HoldingStockService holdingStockService;

    @Scheduled(fixedDelay = 60000)
    public void releaseInvalidHoldingStock() {
        holdingStockService.releaseInvalidHoldingStock();
    }
}
