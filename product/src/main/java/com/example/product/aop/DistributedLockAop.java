package com.example.product.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Aspect
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DistributedLockAop {

    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock)")
    public Object lock(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String key = distributedLock.key();
        RLock lock = redissonClient.getLock(key);

        try {
            boolean isLocked = lock.tryLock(5L, 3L, TimeUnit.SECONDS);
            if (!isLocked) {
                throw new IllegalStateException("락 획득 실패");
            }

            return joinPoint.proceed();
        } finally {
            if (lock.isLocked()) {
                lock.unlock();
            }
        }
    }
}
