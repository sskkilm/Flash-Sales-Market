package com.example.payment.infrastructure.repository;

import com.example.payment.application.port.CacheRepository;
import com.example.payment.infrastructure.repository.cache.TemporaryPaymentInfoCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
@RequiredArgsConstructor
public class CacheRepositoryImpl implements CacheRepository {

    private final TemporaryPaymentInfoCacheRepository temporaryPaymentInfoCacheRepository;

    @Override
    public void saveTemporaryPaymentInfo(Long orderId, BigDecimal amount) {
        temporaryPaymentInfoCacheRepository.save(orderId, amount);
    }

    @Override
    public BigDecimal getTemporaryPaymentInfo(Long orderId) {
        return new BigDecimal(temporaryPaymentInfoCacheRepository.get(orderId));
    }

    @Override
    public void deleteTemporaryPaymentInfo(Long orderId) {
        temporaryPaymentInfoCacheRepository.delete(orderId);
    }
}
