package com.example.order.infrastructure.repository.persistence;

import com.example.order.infrastructure.repository.persistence.entity.OrderEntity;
import com.example.order.infrastructure.repository.persistence.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductJpaRepository extends JpaRepository<OrderProductEntity, Long> {
    List<OrderProductEntity> findAllByOrder(OrderEntity order);
}
