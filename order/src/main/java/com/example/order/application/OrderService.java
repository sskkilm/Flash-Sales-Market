package com.example.order.application;

import com.example.order.application.feign.ProductFeignClient;
import com.example.order.application.port.OrderProductRepository;
import com.example.order.application.port.OrderRepository;
import com.example.order.domain.AmountCalculator;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.*;
import com.example.order.exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.order.exception.error.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductFeignClient productFeignClient;
    private final LocalDateTimeHolder localDateTimeHolder;

    private final AmountCalculator amountCalculator = new AmountCalculator();

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest orderCreateRequest) {

        Order order = orderRepository.save(Order.create(memberId));

        StockPreoccupationResponse stockPreoccupationResponse = productFeignClient.preoccupyStock(
                mapToStockPreoccupationRequest(order, orderCreateRequest)
        );

        List<OrderProduct> orderProducts = orderProductRepository.saveAll(
                mapToOrderProducts(order, stockPreoccupationResponse)
        );
        List<Long> orderProductIds = orderProducts.stream().map(OrderProduct::getId).toList();

        BigDecimal totalAmount = amountCalculator.calculateTotalAmount(orderProducts);

        return new OrderCreateResponse(order.getId(), orderProductIds, totalAmount);
    }

    @Transactional
    public OrderCancelResponse cancel(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId);

        order.cancel(memberId, localDateTimeHolder.now());
        orderRepository.save(order);

        List<ProductRestockInfo> productRestockInfos = orderProductRepository.findAllByOrder(order)
                .stream().map(
                        orderProduct -> new ProductRestockInfo(
                                orderProduct.getProductId(), orderProduct.getQuantity()
                        )
                ).toList();
        productFeignClient.restock(new ProductRestockRequest(productRestockInfos));

        return new OrderCancelResponse(order.getId(), order.getMemberId(), order.getStatus().name());
    }

    public OrderReturnResponse returns(Long memberId, Long orderId) {
        Order order = orderRepository.findById(orderId);

        order.returns(memberId, localDateTimeHolder.now());
        orderRepository.save(order);

        return new OrderReturnResponse(order.getId(), order.getMemberId(), order.getStatus().name());
    }

    public List<OrderHistory> getOrderHistories(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);

        return orders.stream().map(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            BigDecimal totalPrice = amountCalculator.calculateTotalAmount(orderProducts);

            List<OrderProductDto> orderProductDtos = orderProducts.stream().map(OrderProductDto::from).toList();

            return new OrderHistory(order.getId(), order.getMemberId(), order.getStatus().name(), totalPrice, orderProductDtos);

        }).toList();
    }

    @Transactional
    public int updateOrderStatusFromOneDayAgo(OrderStatus currentStatus, OrderStatus newStatus) {
        LocalDateTime now = localDateTimeHolder.now();

        LocalDateTime yesterday = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime today = now.toLocalDate().atStartOfDay();

        return orderRepository.updateOrderStatusBetween(currentStatus, newStatus, yesterday, today);
    }

    @Transactional
    public void returnProcessing() {
        LocalDateTime now = localDateTimeHolder.now();

        LocalDateTime yesterday = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime today = now.toLocalDate().atStartOfDay();
        List<Order> orders = orderRepository.findAllByOrderStatusBetween(
                OrderStatus.RETURN_IN_PROGRESS, yesterday, today
        );

        orders.forEach(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            List<ProductRestockInfo> productRestockInfos = orderProducts.stream().map(
                    orderProduct -> new ProductRestockInfo(
                            orderProduct.getProductId(), orderProduct.getQuantity()
                    )
            ).toList();
            productFeignClient.restock(new ProductRestockRequest(productRestockInfos));

            order.returned();
        });

        orderRepository.saveAll(orders);
    }

    public boolean validateOrderInfo(Long memberId, OrderValidationRequest request) {
        Order order = orderRepository.findById(request.orderId());
        if (order.isNotOrderBy(memberId)) {
            throw new OrderServiceException(MEMBER_UN_MATCHED);
        }
        if (order.isNotPending()) {
            throw new OrderServiceException(ALREADY_PROCESSED);
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        validateOrderProducts(request, orderProducts);

        BigDecimal totalAmount = amountCalculator.calculateTotalAmount(orderProducts);
        if (totalAmountUnMatched(request, totalAmount)) {
            throw new OrderServiceException(TOTAL_AMOUNT_UN_MATCHED);
        }

        return true;
    }

    public void updateOrderCompleted(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.completed();
        orderRepository.save(order);
        log.info("Order ID:{} Completed", orderId);
    }

    public void updateOrderFailed(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.failed();
        orderRepository.save(order);
        log.info("Order ID:{} Failed", orderId);
    }

    private static boolean totalAmountUnMatched(OrderValidationRequest request, BigDecimal totalAmount) {
        return request.amount().compareTo(totalAmount) != 0;
    }

    private static StockPreoccupationRequest mapToStockPreoccupationRequest(Order order, OrderCreateRequest orderCreateRequest) {
        return new StockPreoccupationRequest(
                order.getId(),
                orderCreateRequest.orderInfos().stream().map(
                        orderInfo -> new StockPreoccupationInfo(
                                orderInfo.productId(), orderInfo.quantity()
                        )
                ).toList());
    }

    private static List<OrderProduct> mapToOrderProducts(Order order, StockPreoccupationResponse stockPreoccupationResponse) {
        return stockPreoccupationResponse.stockPreoccupationResults()
                .stream().map(
                        stockHoldResult -> OrderProduct.create(
                                order,
                                stockHoldResult.productId(),
                                stockHoldResult.productName(),
                                stockHoldResult.quantity(),
                                stockHoldResult.amount()
                        )
                ).toList();
    }

    private static void validateOrderProducts(OrderValidationRequest request, List<OrderProduct> orderProducts) {
        Set<Long> orderProductIds = orderProducts.stream().map(OrderProduct::getId).collect(Collectors.toSet());
        Set<Long> requestOrderProductIds = new HashSet<>(request.orderProductIds());
        if (!orderProductIds.equals(requestOrderProductIds)) {
            throw new OrderServiceException(ORDER_PRODUCT_INFO_UN_MATCHED);
        }
    }

}
