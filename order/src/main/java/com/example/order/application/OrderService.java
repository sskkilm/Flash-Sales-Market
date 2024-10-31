package com.example.order.application;

import com.example.order.application.feign.ProductFeignClient;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.*;
import com.example.order.dto.*;
import com.example.order.exception.OrderNotFoundException;
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
    private final ProductFeignClient productFeignClient;
    private final LocalDateTimeHolder holder;
    private final OrderProductManager manager = new OrderProductManager();

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest orderCreateRequest) {
        Order order = orderRepository.save(Order.create(memberId));

        ProductPurchaseResponse productPurchaseResponse = productFeignClient.purchase(
                convertOrderCreateRequestToProductPurchaseRequest(orderCreateRequest)
        );

        List<OrderProduct> orderProducts = orderProductRepository.saveAll(
                productPurchaseResponse.purchasedProductInfos().stream()
                        .map(purchasedProductInfo -> OrderProduct.create(
                                order,
                                purchasedProductInfo.productId(),
                                purchasedProductInfo.productName(),
                                purchasedProductInfo.quantity(),
                                purchasedProductInfo.purchaseAmount()
                        )).toList()
        );

        List<OrderedProductInfo> orderedProductInfos = orderProducts.stream().map(OrderedProductInfo::from).toList();
        return new OrderCreateResponse(order.getId(), order.getMemberId(), order.getStatus().name(), orderedProductInfos);
    }

    @Transactional
    public OrderCancelResponse cancel(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.cancel(memberId, holder.now());
        orderRepository.save(order);

        List<ProductRestoreStockInfo> productRestoreStockInfos = orderProductRepository.findAllByOrder(order)
                .stream().map(
                        orderProduct -> new ProductRestoreStockInfo(
                                orderProduct.getProductId(), orderProduct.getQuantity()
                        )
                ).toList();
        productFeignClient.restoreStock(new ProductRestoreStockRequest(productRestoreStockInfos));

        return new OrderCancelResponse(order.getId(), order.getMemberId(), order.getStatus().name());
    }

    public OrderReturnResponse returns(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.returns(memberId, holder.now());
        orderRepository.save(order);

        return new OrderReturnResponse(order.getId(), order.getMemberId(), order.getStatus().name());
    }

    public List<OrderHistory> getOrderHistories(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);

        return orders.stream().map(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            BigDecimal totalPrice = manager.calculateTotalPrice(orderProducts);

            List<OrderedProductInfo> orderedProductInfos = orderProducts.stream().map(OrderedProductInfo::from).toList();

            return new OrderHistory(order.getId(), order.getMemberId(), order.getStatus().name(), totalPrice, orderedProductInfos);

        }).toList();
    }

    @Transactional
    public int updateOrderStatus(OrderStatus currentStatus, OrderStatus newStatus) {
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = today.atStartOfDay();

        return orderRepository.updateOrderStatusBetween(currentStatus, newStatus, start, end);
    }

    @Transactional
    public void returnProcessing() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        List<Order> orders = orderRepository.findAllByOrderStatusBeforeToday(
                OrderStatus.RETURN_IN_PROGRESS, today
        );

        orders.forEach(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            List<ProductRestoreStockInfo> productRestoreStockInfos = orderProducts.stream().map(
                    orderProduct -> new ProductRestoreStockInfo(
                            orderProduct.getProductId(), orderProduct.getQuantity()
                    )
            ).toList();
            productFeignClient.restoreStock(new ProductRestoreStockRequest(productRestoreStockInfos));

            order.returnCompleted();
        });

        orderRepository.saveAll(orders);
    }

    private static ProductPurchaseRequest convertOrderCreateRequestToProductPurchaseRequest(OrderCreateRequest orderCreateRequest) {
        return new ProductPurchaseRequest(
                orderCreateRequest.productOrderInfos().stream()
                        .map(productOrderInfo -> new ProductPurchaseInfo(
                                productOrderInfo.productId(), productOrderInfo.quantity())
                        ).toList()
        );
    }

}
