package com.example.payment.infrastructure.repository.cache;

import com.example.payment.domain.exception.PaymentServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;

import static com.example.payment.domain.exception.ErrorCode.TEMPORARY_PAYMENT_INFO_NOT_FOUND;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TemporaryPaymentInfoCacheRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final String PAYMENT_AMOUNT_KEY = "orders:%d:amount";

    public void save(Long orderId, BigDecimal amount) {
        String key = generateKey(orderId);
        redisTemplate.opsForValue().set(key, String.valueOf(amount), Duration.ofMinutes(15));
        log.info("{}번 주문에 대한 임시 결제 정보 저장 성공, 금액: {}, TTL: 10분", orderId, amount);
    }

    public String get(Long orderId) {
        String key = generateKey(orderId);
        String amount = redisTemplate.opsForValue().get(key);
        if (amount == null) {
            log.info("임시 결제 정보가 존재하지 않습니다.");
            throw new PaymentServiceException(TEMPORARY_PAYMENT_INFO_NOT_FOUND);
        }
        return amount;
    }

    public void delete(Long orderId) {
        Boolean success = redisTemplate.delete(generateKey(orderId));
        if (success) {
            log.info("{}번 주문에 대한 임시 결제 정보 삭제", orderId);
        }
    }

    private String generateKey(Long orderId) {
        return String.format(PAYMENT_AMOUNT_KEY, orderId);
    }
}
