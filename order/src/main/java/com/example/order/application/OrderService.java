package com.example.order.application;

import com.example.common.dto.ProductPurchaseRequest;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.common.dto.ProductStockRecoveryRequest;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.*;
import com.example.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductService productService;
    private final LocalDateTimeHolder holder;
    private final OrderProductManager manager;

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest orderCreateRequest) {
        Order order = orderRepository.save(Order.create(memberId));

        List<ProductPurchaseResponse> productPurchaseResponses = productService.purchaseProducts(
                orderCreateRequest.orderProducts().stream()
                        .map(request -> new ProductPurchaseRequest(
                                request.productId(), request.quantity())
                        ).toList()
        );

        List<OrderProduct> orderProducts = orderProductRepository.saveAll(
                productPurchaseResponses.stream()
                        .map(response -> OrderProduct.create(
                                order,
                                response.productId(),
                                response.productName(),
                                response.quantity(),
                                response.purchaseAmount()
                        )).toList()
        );

        List<OrderProductResponse> orderProductResponses = orderProducts.stream().map(OrderProductResponse::from).toList();
        return new OrderCreateResponse(order.getId(), order.getMemberId(), order.getStatus(), orderProductResponses);
    }

    @Transactional
    public OrderCancelResponse cancel(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "order not found -> orderId: " + orderId
                ));
        order.cancel(memberId);
        orderRepository.save(order);

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        productService.stockRecovery(orderProducts.stream().map(
                orderProduct -> new ProductStockRecoveryRequest(
                        orderProduct.getProductId(), orderProduct.getQuantity()
                )
        ).toList());

        return new OrderCancelResponse(order.getId(), order.getMemberId(), order.getStatus());
    }

    public OrderReturnResponse returns(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "order not found -> orderId: " + orderId
                ));
        order.returns(memberId, holder);
        orderRepository.save(order);

        return new OrderReturnResponse(order.getId(), order.getMemberId(), order.getStatus());
    }

    public List<OrderHistory> getOrderHistory(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);

        return orders.stream().map(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            BigDecimal totalPrice = manager.calculateTotalPrice(orderProducts);

            List<OrderProductResponse> orderProductResponses = orderProducts.stream().map(OrderProductResponse::from).toList();

            return new OrderHistory(order.getId(), order.getMemberId(), order.getStatus(), totalPrice.toString(), orderProductResponses);
        }).toList();
    }

    @Transactional
    public int updateOrderStatus(OrderStatus currentStatus, OrderStatus newStatus) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = today.atStartOfDay();

        return orderRepository.updateOrderStatus(currentStatus, newStatus, start, end);
    }

    @Transactional
    public void returnProcessing() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<Order> orders = orderRepository.findAllByOrderStatusBeforeToday(
                OrderStatus.RETURN_IN_PROGRESS, today
        );

        orders.forEach(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            productService.stockRecovery(
                    orderProducts.stream().map(
                            orderProduct -> new ProductStockRecoveryRequest(
                                    orderProduct.getProductId(), orderProduct.getQuantity()
                            )
                    ).toList()
            );

            order.returnCompleted();
        });

        orderRepository.saveAll(orders);
    }
}
