package com.example.order.infrastructure.order.product;

import com.example.order.application.repository.OrderProductRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.infrastructure.entity.OrderEntity;
import com.example.order.infrastructure.entity.OrderProductEntity;
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
                orderProducts.stream().map(OrderProductEntity::from).toList()
        ).stream().map(OrderProductEntity::toModel).toList();
    }

    @Override
    public List<OrderProduct> findAllByOrder(Order order) {
        return orderProductJpaRepository.findAllByOrder(OrderEntity.from(order)).stream()
                .map(OrderProductEntity::toModel).toList();
    }

}
