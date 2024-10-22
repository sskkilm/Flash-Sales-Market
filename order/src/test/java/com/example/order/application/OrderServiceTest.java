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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        Order order = Order.builder()
                .id(1L)
                .memberId(1L)
                .status(OrderStatus.ORDER_COMPLETED)
                .build();
        given(orderRepository.save(any(Order.class)))
                .willReturn(order);
        given(productService.purchaseProducts(anyList()))
                .willReturn(
                        List.of(
                                new ProductPurchaseResponse(1L, 1, "name1", Money.of("20000")),
                                new ProductPurchaseResponse(2L, 2, "name2", Money.of("60000"))
                        )
                );
        given(orderProductRepository.saveAll(anyList()))
                .willReturn(List.of(
                        OrderProduct.builder()
                                .order(order)
                                .productId(1L)
                                .quantity(1)
                                .name("name1")
                                .orderAmount(Money.of("20000"))
                                .build(),
                        OrderProduct.builder()
                                .order(order)
                                .productId(2L)
                                .quantity(2)
                                .name("name2")
                                .orderAmount(Money.of("60000"))
                                .build()
                ));
        //when
        OrderCreateResponse response = orderService.create(1L, orderCreateRequest);

        //then
        assertEquals(1L, response.orderId());
        assertEquals(1L, response.memberId());
        assertEquals(OrderStatus.ORDER_COMPLETED, response.status());
        assertEquals(2, response.orderProducts().size());
        assertEquals(1L, response.orderProducts().get(0).productId());
        assertEquals(1, response.orderProducts().get(0).quantity());
        assertEquals("name1", response.orderProducts().get(0).productName());
        assertEquals(Money.of("20000"), response.orderProducts().get(0).orderAmount());
        assertEquals(2L, response.orderProducts().get(1).productId());
        assertEquals(2, response.orderProducts().get(1).quantity());
        assertEquals("name2", response.orderProducts().get(1).productName());
        assertEquals(Money.of("60000"), response.orderProducts().get(1).orderAmount());
    }

    @Test
    void 주문_취소시_존재하지_않는_주문이면_예외가_발생한다() {
        //given
        given(memberService.findById(1L))
                .willReturn(1L);
        given(orderRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                //when
                () -> orderService.cancel(1L, 1L));
    }

}