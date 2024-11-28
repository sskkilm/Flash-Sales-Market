package com.example.order.infrastructure.repository;

import com.example.order.application.port.OrderProductRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.infrastructure.repository.persistence.OrderProductJpaRepository;
import com.example.order.infrastructure.repository.persistence.mapper.OrderMapper;
import com.example.order.infrastructure.repository.persistence.mapper.OrderProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderProductRepositoryImpl implements OrderProductRepository {

    private final OrderProductJpaRepository orderProductJpaRepository;

    @Override
    public List<OrderProduct> saveAll(List<OrderProduct> orderProducts) {
        return orderProductJpaRepository.saveAll(
                orderProducts.stream().map(OrderProductMapper::toEntity).toList()
        ).stream().map(OrderProductMapper::toModel).toList();
    }

    @Override
    public List<OrderProduct> findAllByOrder(Order order) {
        return orderProductJpaRepository.findAllByOrder(OrderMapper.toEntity(order)).stream()
                .map(OrderProductMapper::toModel).toList();
    }

}
