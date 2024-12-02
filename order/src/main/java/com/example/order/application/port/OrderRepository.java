package com.example.order.application.port;

import com.example.order.domain.Order;

import java.util.List;

public interface OrderRepository {
    Order save(Order order);

    Order findById(Long id);

    List<Order> findAllByMemberId(Long memberId);

    List<Long> findIdsByMemberId(Long memberId);
}
