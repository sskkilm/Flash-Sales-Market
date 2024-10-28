package com.example.order.infrastructure;

import com.example.order.infrastructure.entity.OrderEntity;
import com.example.order.infrastructure.entity.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductJpaRepository extends JpaRepository<OrderProductEntity, Long> {
    List<OrderProductEntity> findAllByOrder(OrderEntity order);
}
