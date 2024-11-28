package com.example.order.application;

import com.example.order.application.feign.ProductClient;
import com.example.order.application.port.OrderProductRepository;
import com.example.order.application.port.OrderRepository;
import com.example.order.common.dto.OrderDto;
import com.example.order.common.dto.OrderProductDto;
import com.example.order.common.dto.ProductDto;
import com.example.order.common.dto.request.OrderCreateRequest;
import com.example.order.common.dto.request.StockDecreaseRequest;
import com.example.order.common.dto.request.StockIncreaseRequest;
import com.example.order.common.dto.response.OrderCreateResponse;
import com.example.order.domain.AmountCalculator;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final ProductClient productClient;

    private final AmountCalculator amountCalculator = new AmountCalculator();

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest request) {

        Order order = orderRepository.save(Order.create(memberId));

        List<OrderProduct> orderProducts = getOrderProducts(request, order);
        orderProductRepository.saveAll(orderProducts);

        decreaseStock(request);

        log.info("Order Id:{} Created", order.getId());

        return OrderCreateResponse.from(order);
    }

    public List<OrderDto> getOrderList(Long memberId) {
        List<Order> orders = orderRepository.findAllByMemberId(memberId);

        return orders.stream().map(order -> {
            List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);

            BigDecimal totalPrice = amountCalculator.calculateTotalAmount(orderProducts);

            List<OrderProductDto> orderProductDtos = orderProducts.stream().map(OrderProductDto::from).toList();

            return new OrderDto(order.getId(), order.getMemberId(), order.getStatus().name(), totalPrice, orderProductDtos);

        }).toList();
    }

    public OrderDto findById(Long orderId) {
        Order order = orderRepository.findById(orderId);

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        BigDecimal totalPrice = amountCalculator.calculateTotalAmount(orderProducts);

        List<OrderProductDto> orderProductDtos = orderProducts.stream().map(OrderProductDto::from).toList();

        return new OrderDto(order.getId(), order.getMemberId(), order.getStatus().name(), totalPrice, orderProductDtos);
    }

    @Transactional
    public void paymentFailed(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.paymentFailed();
        orderRepository.save(order);

        increaseStock(order);

        log.info("Order Id:{} Payment Failed", order.getId());
    }

    @Transactional
    public void paymentConfirmed(Long orderId) {
        Order order = orderRepository.findById(orderId);
        order.paymentConfirmed();
        orderRepository.save(order);

        log.info("Order Id:{} Payment Confirmed", order.getId());
    }

    private List<OrderProduct> getOrderProducts(OrderCreateRequest request, Order order) {
        return request.orderInfos()
                .stream()
                .map(orderInfo -> {
                    ProductDto productDto = productClient.getProductInfo(orderInfo.productId());
                    BigDecimal amount = amountCalculator.calculateAmount(orderInfo.quantity(), productDto.price());
                    return OrderProduct.create(
                            order, orderInfo.productId(), productDto.name(), orderInfo.quantity(), amount
                    );
                }).toList();
    }

    private void decreaseStock(OrderCreateRequest request) {
        List<StockDecreaseRequest> stockDecreaseRequests = request.orderInfos()
                .stream()
                .map(orderInfo -> new StockDecreaseRequest(orderInfo.productId(), orderInfo.quantity()))
                .toList();
        productClient.decreaseStock(stockDecreaseRequests);
    }

    private void increaseStock(Order order) {
        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        List<StockIncreaseRequest> stockIncreaseRequests = orderProducts
                .stream().map(
                        orderProduct -> new StockIncreaseRequest(
                                orderProduct.getProductId(), orderProduct.getQuantity()
                        )
                ).toList();
        productClient.increaseStock(stockIncreaseRequests);
    }
}
