package com.example.order.infrastructure.order;

import com.example.order.domain.OrderStatus;
import com.example.order.infrastructure.entity.OrderEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderJpaRepositoryTest {

    @Autowired
    private OrderJpaRepository orderJpaRepository;

    @BeforeEach
    void setUp() {
        // 테스트용 데이터 생성
        OrderEntity order1 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED.name())
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        OrderEntity order2 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED.name())
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();

        OrderEntity order3 = OrderEntity.builder()
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED.name())
                .createdAt(LocalDateTime.now())
                .build();

        orderJpaRepository.save(order1);
        orderJpaRepository.save(order2);
        orderJpaRepository.save(order3);
    }

    @Test
    void testUpdateOrderStatus() {
        // given
        LocalDateTime start = LocalDate.now().minusDays(1).atStartOfDay(); // 어제 00:00
        LocalDateTime end = LocalDate.now().atStartOfDay(); // 오늘 00:00

        // when
        int updatedCount = orderJpaRepository.updateOrderStatus(
                OrderStatus.ORDER_COMPLETED.toString(),
                OrderStatus.DELIVERY_IN_PROGRESS.toString(),
                start, end
        );

        // then
        assertThat(updatedCount).isEqualTo(2);

        List<OrderEntity> updatedOrders = orderJpaRepository.findByStatus(OrderStatus.DELIVERY_IN_PROGRESS.name());
        assertThat(updatedOrders.size()).isEqualTo(2);
    }
}