package com.example.product.aop;

import com.example.product.application.port.LockService;
import com.example.product.dto.StockPreoccupationInfo;
import com.example.product.dto.StockPreoccupationRequest;
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
public class LockProductsAspect {

    private static final String LOCK_KEY_PREFIX = "Lock::Product_";

    private final LockService lockService;

    @Around("@annotation(lockProducts) && args(stockPreoccupationRequest)")
    public Object lock(ProceedingJoinPoint joinPoint, LockProducts lockProducts, StockPreoccupationRequest stockPreoccupationRequest) throws Throwable {
        List<Long> productIds = stockPreoccupationRequest.stockPreoccupationInfos().stream()
                .map(StockPreoccupationInfo::productId)
                .sorted()
                .toList();
        lockService.acquireAllLocks(LOCK_KEY_PREFIX, productIds, lockProducts.waitTime(), lockProducts.leaseTime());

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseAllLocks(LOCK_KEY_PREFIX, productIds);
        }
    }

    @Around("@annotation(lockProducts) && args(orderId, orderProductIds)")
    public Object lock(ProceedingJoinPoint joinPoint, LockProducts lockProducts, Long orderId, List<Long> orderProductIds) throws Throwable {
        List<Long> productIds = orderProductIds.stream()
                .sorted()
                .toList();
        lockService.acquireAllLocks(LOCK_KEY_PREFIX, productIds, lockProducts.waitTime(), lockProducts.leaseTime());

        try {
            return joinPoint.proceed();
        } finally {
            lockService.releaseAllLocks(LOCK_KEY_PREFIX, productIds);
        }
    }

}
