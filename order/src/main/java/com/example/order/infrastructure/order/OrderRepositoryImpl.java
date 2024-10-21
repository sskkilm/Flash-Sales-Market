package com.example.order.infrastructure.order;

import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.infrastructure.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(OrderEntity.from(order)).toModel();
    }
}
