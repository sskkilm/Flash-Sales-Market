package com.example.order.application.repository;

import com.example.order.domain.OrderProduct;

import java.util.List;

public interface OrderProductRepository {
    List<OrderProduct> saveAll(List<OrderProduct> orderProducts);
}
