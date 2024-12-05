package com.example.payment.application;

import com.example.payment.application.port.CacheRepository;
import com.example.payment.common.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheRepository cacheRepository;

    public void saveTemporaryPaymentInfo(OrderDto order) {
        cacheRepository.saveTemporaryPaymentInfo(order.orderId(), order.amount());
    }

    public BigDecimal getTemporaryPaymentInfo(Long orderId) {
        return cacheRepository.getTemporaryPaymentInfo(orderId);
    }

    public void deleteTemporaryPaymentInfo(Long orderId) {
        cacheRepository.deleteTemporaryPaymentInfo(orderId);
    }
}
