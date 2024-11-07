package com.example.payment.infrastructure.external;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class PGEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    Long orderId;

    @Column(nullable = false)
    BigDecimal amount;

    @Column(nullable = false, unique = true)
    String paymentKey;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime createdAt;

    public static PGEntity create(Long orderId, BigDecimal amount, String paymentKey) {
        return PGEntity.builder()
                .orderId(orderId)
                .amount(amount)
                .paymentKey(paymentKey)
                .build();
    }

    public void validate(Long orderId, BigDecimal amount) {
        validateOrderId(orderId);
        validateAmount(amount);
    }

    public PGEntity updatePaymentKey(String paymentKey) {
        this.paymentKey = paymentKey;
        return this;
    }

    private void validateOrderId(Long orderId) {
        if (Objects.equals(this.orderId, orderId)) {
            return;
        }

        throw new RuntimeException("Invalid order id: " + orderId);
    }

    private void validateAmount(BigDecimal amount) {
        if (this.amount.compareTo(amount) == 0) {
            return;
        }

        throw new RuntimeException("Invalid amount: " + amount);
    }
}
