package com.example.order.application;

import com.example.common.dto.ProductPurchaseRequest;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.common.dto.ProductStockRecoveryRequest;
import com.example.member.application.MemberService;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderCreateResponse;
import com.example.order.dto.OrderProductResponse;
import com.example.product.application.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductRepository;
    private final MemberService memberService;
    private final ProductService productService;

    @Transactional
    public OrderCreateResponse create(Long memberId, OrderCreateRequest orderCreateRequest) {
        Long checkedMemberId = memberService.findById(memberId);

        Order order = orderRepository.save(Order.create(checkedMemberId));

        List<ProductPurchaseRequest> productPurchaseRequests = orderCreateRequest.orderProducts().stream()
                .map(orderProductDto -> new ProductPurchaseRequest(
                        orderProductDto.productId(), orderProductDto.quantity())
                ).toList();
        List<ProductPurchaseResponse> productPurchaseResponses = productService.purchaseProducts(productPurchaseRequests);

        List<OrderProduct> orderProducts = productPurchaseResponses.stream()
                .map(response -> OrderProduct.create(
                        order, response.productId(), response.quantity(), response.purchaseAmount()
                )).toList();

        List<OrderProductResponse> orderProductResponses = orderProductRepository.saveAll(orderProducts).stream()
                .map(orderProduct -> new OrderProductResponse(
                        orderProduct.getProductId(), orderProduct.getQuantity(), orderProduct.getOrderAmount()
                )).toList();
        return new OrderCreateResponse(order.getId(), order.getMemberId(), order.getStatus(), orderProductResponses);
    }

    public void cancel(Long memberId, Long orderId) {
        Long checkedMemberId = memberService.findById(memberId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "order not found -> orderId: " + orderId
                ));
        if (order.isNotOrderedBy(checkedMemberId)) {
            throw new IllegalArgumentException(
                    "this order is not ordered by this member -> memberId: " + checkedMemberId
            );
        }

        if (order.canNotBeCanceled()) {
            throw new IllegalArgumentException("order can not be canceled");
        }

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        orderProductRepository.deleteAll(orderProducts);

        productService.stockRecovery(orderProducts.stream().map(
                orderProduct -> new ProductStockRecoveryRequest(
                        orderProduct.getProductId(), orderProduct.getQuantity()
                )
        ).toList());

        order.canceled();
        orderRepository.save(order);
    }
}
