package com.example.payment.infrastructure;

import com.example.payment.infrastructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    boolean existsByOrderId(Long orderId);
}
