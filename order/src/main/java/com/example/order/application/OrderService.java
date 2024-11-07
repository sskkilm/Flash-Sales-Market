package com.example.order.application;

import com.example.order.application.feign.ProductFeignClient;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.domain.OrderProductManager;
import com.example.order.domain.OrderStatus;
import com.example.order.dto.*;
import com.example.order.exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.example.order.exception.error.ErrorCode.MEMBER_UN_MATCHED;
import static com.example.order.exception.error.ErrorCode.TOTAL_AMOUNT_MIS_MATCH;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductFeignClient productFeignClient;
    private final LocalDateTimeHolder localDateTimeHolder;

    private final OrderProductManager orderProductManager = new OrderProductManager();

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest orderCreateRequest) {

        ProductOrderResponse productOrderResponse = productFeignClient.getProductOrderInfo(
                mapOrderCreateRequestToProductOrderRequest(orderCreateRequest)
        );

        Order order = orderRepository.save(Order.create(memberId));

        List<OrderProduct> orderProducts = mapProductOrderResponseToOrderProducts(productOrderResponse, order);
        orderProductRepository.saveAll(orderProducts);

        BigDecimal totalAmount = orderProductManager.calculateTotalAmount(orderProducts);

        return new OrderCreateResponse(order.getId(), totalAmount);
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

            BigDecimal totalPrice = orderProductManager.calculateTotalAmount(orderProducts);

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

    private ProductOrderRequest mapOrderCreateRequestToProductOrderRequest(OrderCreateRequest orderCreateRequest) {
        return new ProductOrderRequest(
                orderCreateRequest.productInfos().stream().map(
                        productOrderInfo -> new ProductOrderInfo(
                                productOrderInfo.productId(), productOrderInfo.quantity())
                ).toList()
        );
    }

    private static List<OrderProduct> mapProductOrderResponseToOrderProducts(ProductOrderResponse productOrderResponse, Order order) {
        return productOrderResponse.orderedProductInfos().stream().map(
                orderedProductInfo -> OrderProduct.create(
                        order,
                        orderedProductInfo.productId(),
                        orderedProductInfo.productName(),
                        orderedProductInfo.quantity(),
                        orderedProductInfo.amount()
                )
        ).toList();
    }

    public boolean validateOrderInfo(Long memberId, OrderValidationRequest request) throws OrderServiceException {
        Long orderId = request.orderId();
        Order order = orderRepository.findById(orderId);
        if (order.isNotOrderBy(memberId)) {
            throw new OrderServiceException(MEMBER_UN_MATCHED);
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        BigDecimal totalAmount = orderProductManager.calculateTotalAmount(orderProducts);
        if (totalAmountMisMatch(request, totalAmount)) {
            throw new OrderServiceException(TOTAL_AMOUNT_MIS_MATCH);
        }

        return true;
    }

    private static boolean totalAmountMisMatch(OrderValidationRequest request, BigDecimal totalAmount) {
        return request.amount().compareTo(totalAmount) != 0;
    }
}
