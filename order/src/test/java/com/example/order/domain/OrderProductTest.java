package com.example.order.domain;

import com.example.common.domain.Money;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderProductTest {

    @Test
    void 주문_상품_생성() {
        //given
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();

        //when
        OrderProduct orderProduct = OrderProduct.create(order, 1L, 10, Money.of("10000"));

        //then
        assertNull(orderProduct.getId());
        assertEquals(order, orderProduct.getOrder());
        assertEquals(1L, orderProduct.getProductId());
        assertEquals(10, orderProduct.getQuantity());
        assertEquals(Money.of("10000"), orderProduct.getOrderAmount());
        assertNull(orderProduct.getCreatedAt());
    }
}