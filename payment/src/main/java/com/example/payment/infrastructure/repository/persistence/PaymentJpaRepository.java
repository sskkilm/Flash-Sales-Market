package com.example.payment.infrastructure.repository.persistence;

import com.example.payment.infrastructure.repository.persistence.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, Long> {

    Optional<PaymentEntity> findByOrderId(Long orderId);

    @Query("select p from Payment p where p.orderId in :orderIds")
    List<PaymentEntity> findByOrderIdInOrderIds(@Param("orderIds") List<Long> orderIds);
}
