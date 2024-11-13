package com.example.product.aop;

import com.example.product.dto.ProductOrderInfo;
import com.example.product.dto.ProductOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAspect {

    private static final String LOCK_KEY_PREFIX = "Lock::Product_";

    private final RedissonClient redissonClient;

    @Around("@annotation(lockProducts) && args(productOrderRequest)")
    public Object lock(ProceedingJoinPoint joinPoint, LockProducts lockProducts, ProductOrderRequest productOrderRequest) throws Throwable {
        List<Long> productIds = productOrderRequest.productOrderInfos().stream()
                .map(ProductOrderInfo::productId)
                .sorted()
                .toList();

        List<RLock> locks = new ArrayList<>();
        for (Long productId : productIds) {
            RLock lock = redissonClient.getLock(LOCK_KEY_PREFIX + productId);
            boolean isLocked = lock.tryLock(
                    lockProducts.waitTime(), lockProducts.leaseTime(), lockProducts.timeUnit()
            );
            if (!isLocked) {
                log.info("Lock failed for productId: {}", productId);
                // TODO: LockAcquisitionException 으로 변경
                throw new RuntimeException("Lock failed for productId: " + productId);
            }
            locks.add(lock);
        }

        try {
            return joinPoint.proceed();
        } finally {
            releaseAllLocks(locks);
        }
    }

    private void releaseAllLocks(List<RLock> locks) {
        for (RLock lock : locks) {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
