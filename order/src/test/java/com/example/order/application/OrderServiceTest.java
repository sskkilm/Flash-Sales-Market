package com.example.order.application;

import com.example.order.application.feign.ProductFeignClient;
import com.example.order.application.port.OrderProductRepository;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.dto.*;
import com.example.order.exception.OrderServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.order.domain.OrderStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
                List.of(new OrderInfo(1L, 1))
        );

        given(productFeignClient.preoccupyStock(any(StockPreoccupationRequest.class)))
                .willReturn(new StockPreoccupationResponse(List.of(
                        new StockPreoccupationResult(
                                1L, "name", 1, new BigDecimal("10000")
                        )
                )));

        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(PENDING)
                .build();
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);

        //when
        OrderCreateResponse orderCreateResponse = orderService.create(1L, orderCreateRequest);

        //then
        assertEquals(1L, orderCreateResponse.orderId());
        assertEquals(new BigDecimal("10000"), orderCreateResponse.totalAmount());
    }

    @Test
    void 주문_취소시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(orderRepository.findById(1L))
                .willThrow(OrderServiceException.class);

        //then
        assertThrows(OrderServiceException.class,
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
                .status(COMPLETED)
                .createdAt(orderedDateTime)
                .build();
        given(orderRepository.findById(1L)).willReturn(order);

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
        assertEquals(CANCELED.name(), orderCancelResponse.status());
    }

    @Test
    void 반품_시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(orderRepository.findById(1L))
                .willThrow(OrderServiceException.class);

        //then
        assertThrows(OrderServiceException.class,
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
                .willReturn(
                        Order.builder()
                                .id(1L)
                                .memberId(1L)
                                .status(DELIVERED)
                                .updatedAt(deliveryCompletedDateTime)
                                .build()
                );

        LocalDateTime returnedDateTime = deliveryCompletedDateTime.plusHours(1);
        given(localDateTimeHolder.now()).willReturn(returnedDateTime);

        //when
        OrderReturnResponse orderReturnResponse = orderService.returns(1L, 1L);

        //then
        assertEquals(1L, orderReturnResponse.orderId());
        assertEquals(1L, orderReturnResponse.memberId());
        assertEquals(RETURN_IN_PROGRESS.name(), orderReturnResponse.status());
    }

    @Test
    void 주문_내역을_조회한다() {
        //given
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(COMPLETED)
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
        assertEquals(COMPLETED.name(), orderHistory.status());
        assertEquals(new BigDecimal("30000"), orderHistory.totalPrice());

        List<OrderProductDto> orderProductDtos = orderHistory.orderProducts();
        assertEquals(2, orderProductDtos.size());

        OrderProductDto orderProductDto1 = orderProductDtos.get(0);
        assertEquals(1L, orderProductDto1.orderProductId());
        assertEquals("name1", orderProductDto1.productName());
        assertEquals(1, orderProductDto1.quantity());
        assertEquals(new BigDecimal("10000"), orderProductDto1.orderAmount());

        OrderProductDto orderProductDto2 = orderProductDtos.get(1);
        assertEquals(2L, orderProductDto2.orderProductId());
        assertEquals("name2", orderProductDto2.productName());
        assertEquals(2, orderProductDto2.quantity());
        assertEquals(new BigDecimal("20000"), orderProductDto2.orderAmount());
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
                COMPLETED, DELIVERY_IN_PROGRESS, yesterday, today
        )).willReturn(10);

        //when
        int count = orderService.updateOrderStatusFromOneDayAgo(COMPLETED, DELIVERY_IN_PROGRESS);

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
                .status(RETURN_IN_PROGRESS)
                .build();
        given(orderRepository.findAllByOrderStatusBetween(RETURN_IN_PROGRESS, yesterday, today))
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
        assertEquals(RETURNED, order.getStatus());
    }

    @Test
    void 주문_정보_검증_시_회원_정보가_일치하지_않으면_예외를_반환한다() {
        //given
        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                1L, new BigDecimal("10000")
        );
        given(orderRepository.findById(1L))
                .willReturn(
                        Order.builder()
                                .memberId(2L)
                                .build()
                );
        //then
        assertThrows(OrderServiceException.class,
                //when
                () -> orderService.validateOrderInfo(1L, orderValidationRequest));
    }

    @Test
    void 주문_정보_검증_시_총_주문_금액이_일치하지_않으면_예외를_반환한다() {
        //given
        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                1L, new BigDecimal("20000")
        );
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .build();
        given(orderRepository.findById(1L))
                .willReturn(order);
        given(orderProductRepository.findAllByOrder(order))
                .willReturn(List.of(
                        OrderProduct.builder()
                                .orderAmount(new BigDecimal("10000"))
                                .build(),
                        OrderProduct.builder()
                                .orderAmount(new BigDecimal("10000"))
                                .build(),
                        OrderProduct.builder()
                                .orderAmount(new BigDecimal("10000"))
                                .build()
                ));

        //then
        assertThrows(OrderServiceException.class,
                //when
                () -> orderService.validateOrderInfo(1L, orderValidationRequest));
    }

    @Test
    void 주문_정보가_일치하면_true를_반환한다() {
        //given
        OrderValidationRequest orderValidationRequest = new OrderValidationRequest(
                1L, new BigDecimal("20000")
        );
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .build();
        given(orderRepository.findById(1L))
                .willReturn(order);
        given(orderProductRepository.findAllByOrder(order))
                .willReturn(List.of(
                        OrderProduct.builder()
                                .orderAmount(new BigDecimal("10000"))
                                .build(),
                        OrderProduct.builder()
                                .orderAmount(new BigDecimal("10000"))
                                .build()
                ));

        //when
        boolean result = orderService.validateOrderInfo(1L, orderValidationRequest);

        //then
        assertTrue(result);
    }
}