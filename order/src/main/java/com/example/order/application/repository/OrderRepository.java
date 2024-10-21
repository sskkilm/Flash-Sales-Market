package com.example.order.application.repository;

import com.example.order.domain.Order;

public interface OrderRepository {
    Order save(Order order);
}
