package com.example.order.application;

import com.example.common.domain.Money;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.member.application.MemberService;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderCreateResponse;
import com.example.order.dto.OrderProductRequest;
import com.example.product.application.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MemberService memberService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductService productService;

    @Mock
    OrderProductRepository orderProductRepository;

    @InjectMocks
    OrderService orderService;

    @Test
    void 상품을_주문한다() {
        //given
        OrderCreateRequest orderCreateRequest = new OrderCreateRequest(
                List.of(
                        new OrderProductRequest(1L, 1),
                        new OrderProductRequest(2L, 2)
                )
        );
        given(memberService.findById(1L))
                .willReturn(1L);
        given(orderRepository.save(any(Order.class)))
                .willReturn(Order.builder()
                        .id(1L)
                        .memberId(1L)
                        .status(OrderStatus.ORDER_COMPLETED)
                        .build());
        given(productService.purchaseProducts(anyList()))
                .willReturn(
                        List.of(
                                new ProductPurchaseResponse(1L, 1, Money.of("20000")),
                                new ProductPurchaseResponse(2L, 2, Money.of("60000"))
                        )
                );
        given(orderProductRepository.saveAll(anyList()))
                .willReturn(
                        List.of(
                                OrderProduct.builder()
                                        .productId(1L)
                                        .quantity(1)
                                        .orderAmount(Money.of("20000"))
                                        .build(),
                                OrderProduct.builder()
                                        .productId(2L)
                                        .quantity(2)
                                        .orderAmount(Money.of("60000"))
                                        .build()
                        )
                );
        //when
        OrderCreateResponse response = orderService.order(1L, orderCreateRequest);

        //then
        assertEquals(1L, response.orderId());
        assertEquals(1L, response.memberId());
        assertEquals(OrderStatus.ORDER_COMPLETED, response.status());
        assertEquals(2, response.orderProductResponses().size());
        assertEquals(1L, response.orderProductResponses().get(0).productId());
        assertEquals(1, response.orderProductResponses().get(0).quantity());
        assertEquals(Money.of("20000"), response.orderProductResponses().get(0).orderAmount());
        assertEquals(2L, response.orderProductResponses().get(1).productId());
        assertEquals(2, response.orderProductResponses().get(1).quantity());
        assertEquals(Money.of("60000"), response.orderProductResponses().get(1).orderAmount());
    }

}