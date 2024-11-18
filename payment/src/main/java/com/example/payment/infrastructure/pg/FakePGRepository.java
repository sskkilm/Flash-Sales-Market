package com.example.payment.infrastructure.pg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FakePGRepository extends JpaRepository<PGEntity, Long> {
    Optional<PGEntity> findByPaymentKey(String paymentKey);

    Optional<PGEntity> findByOrderId(Long orderId);
}
