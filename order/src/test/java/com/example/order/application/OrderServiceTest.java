package com.example.order.application;

import com.example.order.application.feign.ProductFeignClient;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.*;
import com.example.order.exception.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderProductRepository orderProductRepository;

    @Mock
    ProductFeignClient productFeignClient;

    @Mock
    LocalDateTimeHolder localDateTimeHolder;

    @InjectMocks
    OrderService orderService;

    @Test
    void 상품을_주문한다() {
        //given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                List.of(new ProductOrderInfo(1L, 1))
        );

        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);

        given(productFeignClient.purchase(any(ProductPurchaseRequest.class)))
                .willReturn(new ProductPurchaseResponse(List.of(
                        new PurchasedProductInfo(
                                1L, "name", 1, new BigDecimal("10000")
                        )
                )));

        given(orderProductRepository.saveAll(anyList()))
                .willReturn(List.of(
                        OrderProduct.builder()
                                .id(1L)
                                .order(order)
                                .productId(1L)
                                .name("name")
                                .quantity(1)
                                .orderAmount(new BigDecimal("10000"))
                                .build()
                ));

        //when
        OrderCreateResponse orderCreateResponse = orderService.create(1L, orderCreateRequest);

        //then
        assertEquals(1L, orderCreateResponse.orderId());
        assertEquals(1L, orderCreateResponse.memberId());
        assertEquals(OrderStatus.ORDER_COMPLETED.name(), orderCreateResponse.status());

        assertEquals(1, orderCreateResponse.orderedProductInfos().size());

        OrderedProductInfo orderedProductInfo = orderCreateResponse.orderedProductInfos().getFirst();
        assertEquals(1L, orderedProductInfo.orderProductId());
        assertEquals("name", orderedProductInfo.productName());
        assertEquals(1, orderedProductInfo.quantity());
        assertEquals(new BigDecimal("10000"), orderedProductInfo.orderAmount());
    }

    @Test
    void 주문_취소시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(orderRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(OrderNotFoundException.class,
                //when
                () -> orderService.cancel(1L, 1L));
    }

    @Test
    void 주문을_취소한다() {
        //given
        LocalDateTime orderedDateTime = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .createdAt(orderedDateTime)
                .build();
        given(orderRepository.findById(1L))
                .willReturn(Optional.of(order));

        LocalDateTime canceledDateTime = orderedDateTime.plusHours(1);
        given(localDateTimeHolder.now()).willReturn(canceledDateTime);

        given(orderProductRepository.findAllByOrder(order))
                .willReturn(
                        List.of(
                                OrderProduct.builder()
                                        .productId(1L)
                                        .quantity(1)
                                        .build()
                        )
                );

        //when
        OrderCancelResponse orderCancelResponse = orderService.cancel(1L, 1L);

        //then
        assertEquals(1L, orderCancelResponse.orderId());
        assertEquals(1L, orderCancelResponse.memberId());
        assertEquals(OrderStatus.ORDER_CANCELED.name(), orderCancelResponse.status());
    }

    @Test
    void 반품_시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(orderRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(OrderNotFoundException.class,
                //when
                () -> orderService.returns(1L, 1L));
    }

    @Test
    void 반품한다() {
        //given
        LocalDateTime deliveryCompletedDateTime = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        given(orderRepository.findById(1L))
                .willReturn(Optional.of(
                        Order.builder()
                                .id(1L)
                                .memberId(1L)
                                .status(OrderStatus.DELIVERY_COMPLETED)
                                .updatedAt(deliveryCompletedDateTime)
                                .build())
                );

        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusHours(1);
        given(localDateTimeHolder.now()).willReturn(returnedDateTime);

        //when
        OrderReturnResponse orderReturnResponse = orderService.returns(1L, 1L);

        //then
        assertEquals(1L, orderReturnResponse.orderId());
        assertEquals(1L, orderReturnResponse.memberId());
        assertEquals(OrderStatus.RETURN_IN_PROGRESS.name(), orderReturnResponse.status());
    }

    @Test
    void 주문_내역을_조회한다() {
        //given
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();
        given(orderRepository.findAllByMemberId(1L))
                .willReturn(List.of(order));
        given(orderProductRepository.findAllByOrder(order))
                .willReturn(List.of(
                        OrderProduct.builder()
                                .id(1L)
                                .order(order)
                                .name("name1")
                                .quantity(1)
                                .orderAmount(new BigDecimal("10000"))
                                .build(),
                        OrderProduct.builder()
                                .id(2L)
                                .order(order)
                                .name("name2")
                                .quantity(2)
                                .orderAmount(new BigDecimal("20000"))
                                .build()
                ));

        //when
        List<OrderHistory> orderHistories = orderService.getOrderHistories(1L);

        //then
        assertEquals(1, orderHistories.size());

        OrderHistory orderHistory = orderHistories.getFirst();
        assertEquals(1L, orderHistory.orderId());
        assertEquals(1L, orderHistory.memberId());
        assertEquals(OrderStatus.ORDER_COMPLETED.name(), orderHistory.status());
        assertEquals(new BigDecimal("30000"), orderHistory.totalPrice());

        List<OrderedProductInfo> orderedProductInfos = orderHistory.orderProducts();
        assertEquals(2, orderedProductInfos.size());

        OrderedProductInfo orderedProductInfo1 = orderedProductInfos.get(0);
        assertEquals(1L, orderedProductInfo1.orderProductId());
        assertEquals("name1", orderedProductInfo1.productName());
        assertEquals(1, orderedProductInfo1.quantity());
        assertEquals(new BigDecimal("10000"), orderedProductInfo1.orderAmount());

        OrderedProductInfo orderedProductInfo2 = orderedProductInfos.get(1);
        assertEquals(2L, orderedProductInfo2.orderProductId());
        assertEquals("name2", orderedProductInfo2.productName());
        assertEquals(2, orderedProductInfo2.quantity());
        assertEquals(new BigDecimal("20000"), orderedProductInfo2.orderAmount());
    }

    @Test
    void 하루_전_주문_상태를_변경한다() {
        //given
        LocalDateTime now = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        given(localDateTimeHolder.now())
                .willReturn(now);

        LocalDateTime yesterday = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime today = now.toLocalDate().atStartOfDay();

        given(orderRepository.updateOrderStatusBetween(
                OrderStatus.ORDER_COMPLETED, OrderStatus.DELIVERY_IN_PROGRESS, yesterday, today
        )).willReturn(10);

        //when
        int count = orderService.updateOrderStatusFromOneDayAgo(OrderStatus.ORDER_COMPLETED, OrderStatus.DELIVERY_IN_PROGRESS);

        //then
        assertEquals(10, count);
    }

    @Test
    void 반품_처리를_진행한다() {
        //given
        LocalDateTime now = LocalDateTime.of(
                2024, 10, 31, 12, 0, 0
        );
        given(localDateTimeHolder.now())
                .willReturn(now);

        LocalDateTime yesterday = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime today = now.toLocalDate().atStartOfDay();

        Order order = Order.builder()
                .status(OrderStatus.RETURN_IN_PROGRESS)
                .build();
        given(orderRepository.findAllByOrderStatusBetween(OrderStatus.RETURN_IN_PROGRESS, yesterday, today))
                .willReturn(
                        List.of(order)
                );
        given(orderProductRepository.findAllByOrder(order))
                .willReturn(
                        List.of(
                                OrderProduct.builder()
                                        .productId(1L)
                                        .quantity(1)
                                        .build()
                        )
                );

        //when
        orderService.returnProcessing();

        //then
        assertEquals(OrderStatus.RETURN_COMPLETED, order.getStatus());
    }
}