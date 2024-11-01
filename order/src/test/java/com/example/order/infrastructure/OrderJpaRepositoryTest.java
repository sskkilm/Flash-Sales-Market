package com.example.order.infrastructure;

import com.example.order.domain.OrderStatus;
import com.example.order.infrastructure.entity.OrderEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OrderJpaRepositoryTest {

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Test
    void 특정_기간_사이에_생성된_주문의_주문_상태를_변경한다() {
        //given
        LocalDateTime createdAt = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        OrderEntity order1 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED.name())
                .createdAt(createdAt)
                .build();
        OrderEntity order2 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED.name())
                .createdAt(createdAt)
                .build();
        OrderEntity order3 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.COMPLETED.name())
                .createdAt(createdAt)
                .build();
        orderJpaRepository.saveAll(List.of(order1, order2, order3));

        LocalDateTime tomorrow = createdAt.plusDays(1);

        LocalDateTime start = createdAt.toLocalDate().atStartOfDay();
        LocalDateTime end = tomorrow.toLocalDate().atStartOfDay();

        //when
        int count = orderJpaRepository.updateOrderStatusBetween(
                OrderStatus.COMPLETED.name(), OrderStatus.DELIVERY_IN_PROGRESS.name(), start, end
        );

        List<OrderEntity> orderEntities = orderJpaRepository.findAllById(
                List.of(order1.getId(), order2.getId(), order3.getId())
        );

        //then
        assertEquals(3, count);
        assertEquals(OrderStatus.DELIVERY_IN_PROGRESS.name(), orderEntities.get(0).getStatus());
        assertEquals(OrderStatus.DELIVERY_IN_PROGRESS.name(), orderEntities.get(1).getStatus());
        assertEquals(OrderStatus.DELIVERY_IN_PROGRESS.name(), orderEntities.get(2).getStatus());
    }

    @Test
    void 수정_일자가_특정_기간_사이에_있고_해당_주문_상태에_맞는_주문을_조회한다() {
        //given
        LocalDateTime updated = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        OrderEntity order1 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.RETURN_IN_PROGRESS.name())
                .updatedAt(updated)
                .build();
        OrderEntity order2 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.RETURN_IN_PROGRESS.name())
                .updatedAt(updated)
                .build();
        OrderEntity order3 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.RETURN_IN_PROGRESS.name())
                .updatedAt(updated)
                .build();

        LocalDateTime tomorrow = updated.plusDays(1);

        LocalDateTime start = updated.toLocalDate().atStartOfDay();
        LocalDateTime end = tomorrow.toLocalDate().atStartOfDay();

        orderJpaRepository.saveAll(List.of(order1, order2, order3));

        //when
        List<OrderEntity> orderEntities = orderJpaRepository.findAllByOrderStatusBetween(
                OrderStatus.RETURN_IN_PROGRESS.name(), start, end
        );

        //then
        assertEquals(3, orderEntities.size());
    }
}