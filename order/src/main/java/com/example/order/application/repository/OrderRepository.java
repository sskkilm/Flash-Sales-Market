package com.example.order.application.repository;

import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    Order findById(Long id);

    List<Order> findAllByMemberId(Long memberId);

    int updateOrderStatusBetween(
            OrderStatus currentStatus, OrderStatus newStatus, LocalDateTime start, LocalDateTime end
    );

    List<Order> findAllByOrderStatusBetween(OrderStatus orderStatus, LocalDateTime start, LocalDateTime end);

    void saveAll(List<Order> orders);
}
