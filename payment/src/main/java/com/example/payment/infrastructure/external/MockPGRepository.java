package com.example.payment.infrastructure.external;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MockPGRepository extends JpaRepository<PGEntity, Long> {
    Optional<PGEntity> findByPaymentKey(String paymentKey);

    Optional<PGEntity> findByOrderId(Long orderId);
}