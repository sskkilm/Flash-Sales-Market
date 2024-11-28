package com.example.product.common.aop;

import com.example.product.application.port.LockService;
import com.example.product.common.dto.request.StockDecreaseRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAspect {

    private static final String LOCK_KEY = "products:%d:lock";

    private final LockService lockService;

    @Around("@annotation(distributedLock) && args(requests)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock, List<StockDecreaseRequest> requests) throws Throwable {
        List<Long> productIds = requests
                .stream()
                .map(StockDecreaseRequest::productId)
                .sorted()
                .toList();
        lockService.acquireAllLocks(LOCK_KEY, productIds, distributedLock.waitTime(), distributedLock.leaseTime());

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseAllLocks(LOCK_KEY, productIds);
        }
    }

}
