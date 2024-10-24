package com.example.order.application.repository;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order order);

    Optional<Order> findById(Long id);

    List<Order> findAllByMemberId(Long memberId);

    int updateOrderStatus(
            OrderStatus currentStatus, OrderStatus newStatus, LocalDateTime start, LocalDateTime end
    );
}
