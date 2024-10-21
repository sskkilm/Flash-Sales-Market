package com.example.order.infrastructure.order.product;

import com.example.order.infrastructure.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductJpaRepository extends JpaRepository<OrderProductEntity, Long> {
}
