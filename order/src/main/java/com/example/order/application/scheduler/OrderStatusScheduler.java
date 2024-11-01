package com.example.order.application.scheduler;

import com.example.order.application.OrderService;
import com.example.order.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 0 9 * * *")
    public void updateOrderStatusToDeliveryInProgress() {
        orderService.updateOrderStatusFromOneDayAgo(OrderStatus.COMPLETED, OrderStatus.DELIVERY_IN_PROGRESS);
    }
}
