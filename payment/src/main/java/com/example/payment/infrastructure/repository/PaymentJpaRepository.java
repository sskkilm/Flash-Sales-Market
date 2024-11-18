package com.example.payment.infrastructure.repository;

import com.example.payment.infrastructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByOrderId(Long orderId);

    Optional<PaymentEntity> findByOrderId(Long orderId);
}
