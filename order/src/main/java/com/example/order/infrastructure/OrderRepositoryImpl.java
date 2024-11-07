package com.example.order.infrastructure;

import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderStatus;
import com.example.order.exception.OrderServiceException;
import com.example.order.infrastructure.entity.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.order.exception.error.ErrorCode.*;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(OrderEntity.from(order)).toModel();
    }

    @Override
    public Order findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderEntity::toModel)
                .orElseThrow(() -> new OrderServiceException(ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> findAllByMemberId(Long memberId) {
        return orderJpaRepository.findAllByMemberId(memberId)
                .stream().map(OrderEntity::toModel).toList();
    }

    @Override
    public int updateOrderStatusBetween(OrderStatus currentStatus, OrderStatus newStatus, LocalDateTime start, LocalDateTime end) {
        return orderJpaRepository.updateOrderStatusBetween(
                currentStatus.name(), newStatus.name(), start, end
        );
    }

    @Override
    public List<Order> findAllByOrderStatusBetween(OrderStatus orderStatus, LocalDateTime start, LocalDateTime end) {
        return orderJpaRepository.findAllByOrderStatusBetween(orderStatus.name(), start, end)
                .stream().map(OrderEntity::toModel).toList();
    }

    @Override
    public void saveAll(List<Order> orders) {
        orderJpaRepository.saveAll(
                orders.stream().map(OrderEntity::from).toList()
        );
    }

}
