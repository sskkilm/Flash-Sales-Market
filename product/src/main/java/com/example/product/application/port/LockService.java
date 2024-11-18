package com.example.product.application.port;

import java.util.List;

public interface LockService {
    void acquireAllLocks(String lockKeyPrefix, List<Long> ids, long waitTime, long leaseTime);

    void releaseAllLocks(String lockKeyPrefix, List<Long> ids);
}
