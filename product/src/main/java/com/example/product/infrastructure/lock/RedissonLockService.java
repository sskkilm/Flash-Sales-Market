package com.example.product.infrastructure.lock;

import com.example.product.application.port.LockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedissonLockService implements LockService {

    private final RedissonClient redissonClient;

    @Override
    public void acquireAllLocks(String lockKey, List<Long> ids, long waitTime, long leaseTime) {
        for (Long id : ids) {
            String key = String.format(lockKey, id);
            RLock lock = redissonClient.getLock(key);
            try {
                boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
                if (!isLocked) {
                    log.info("Product Id:{} Lock Acquisition Failed", id);
                    // TODO: LockAcquisitionException 으로 변경
                    throw new RuntimeException("Product Id:" + id + " Lock Acquisition Failed");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException("인터럽트 발생", e);
            }
        }
    }

    @Override
    public void releaseAllLocks(String lockKey, List<Long> ids) {
        for (Long id : ids) {
            String key = String.format(lockKey, id);
            RLock lock = redissonClient.getLock(key);
            if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}
