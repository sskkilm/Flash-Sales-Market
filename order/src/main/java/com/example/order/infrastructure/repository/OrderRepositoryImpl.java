package com.example.order.infrastructure.repository;

import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.exception.OrderServiceException;
import com.example.order.infrastructure.repository.persistence.OrderJpaRepository;
import com.example.order.infrastructure.repository.persistence.entity.OrderEntity;
import com.example.order.infrastructure.repository.persistence.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.order.domain.exception.ErrorCode.ORDER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return OrderMapper.toModel(
                orderJpaRepository.save(
                        OrderMapper.toEntity(order)
                )
        );
    }

    @Override
    public Order findById(Long id) {
        return orderJpaRepository.findById(id).map(OrderMapper::toModel)
                .orElseThrow(() -> new OrderServiceException(ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> findAllByMemberId(Long memberId) {
        return orderJpaRepository.findAllByMemberId(memberId)
                .stream().map(OrderMapper::toModel).toList();
    }

    @Override
    public List<Long> findIdsByMemberId(Long memberId) {
        return orderJpaRepository.findAllByMemberId(memberId)
                .stream()
                .map(OrderEntity::getId)
                .toList();
    }
}
