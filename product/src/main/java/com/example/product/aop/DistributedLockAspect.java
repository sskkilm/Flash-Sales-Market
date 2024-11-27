package com.example.product.aop;

import com.example.product.application.port.LockService;
import com.example.product.domain.Product;
import com.example.product.dto.StockPreoccupationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAspect {

    private static final String LOCK_KEY_PREFIX = "Lock::Product_";

    private final LockService lockService;

    @Around("@annotation(distributedLock) && args(orderId, productMap, stockPreoccupationInfos)")
    public Object lockByStockPreoccupationInfos(ProceedingJoinPoint joinPoint, DistributedLock distributedLock,
                                                Long orderId, Map<Long, Product> productMap, List<StockPreoccupationInfo> stockPreoccupationInfos) throws Throwable {
        List<Long> productIds = stockPreoccupationInfos.stream()
                .map(StockPreoccupationInfo::productId)
                .sorted()
                .toList();
        lockService.acquireAllLocks(LOCK_KEY_PREFIX, productIds, distributedLock.waitTime(), distributedLock.leaseTime());

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseAllLocks(LOCK_KEY_PREFIX, productIds);
        }
    }

    @Around("@annotation(distributedLock) && args(orderId, orderProductIds)")
    public Object lockByOrderProductIds(ProceedingJoinPoint joinPoint, DistributedLock distributedLock,
                                        Long orderId, List<Long> orderProductIds) throws Throwable {
        List<Long> productIds = orderProductIds.stream()
                .sorted()
                .toList();
        lockService.acquireAllLocks(LOCK_KEY_PREFIX, productIds, distributedLock.waitTime(), distributedLock.leaseTime());

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseAllLocks(LOCK_KEY_PREFIX, productIds);
        }
    }

}
