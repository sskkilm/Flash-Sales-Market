package com.example.payment.application.port;

import java.math.BigDecimal;

public interface CacheRepository {
    void saveTemporaryPaymentInfo(Long orderId, BigDecimal amount);

    BigDecimal getTemporaryPaymentInfo(Long orderId);

    void deleteTemporaryPaymentInfo(Long orderId);
}
