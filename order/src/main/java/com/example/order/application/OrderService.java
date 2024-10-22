package com.example.order.application;

import com.example.common.dto.ProductPurchaseRequest;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.common.dto.ProductStockRecoveryRequest;
import com.example.member.application.MemberService;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.dto.OrderCancelResponse;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderCreateResponse;
import com.example.order.dto.OrderProductDto;
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

        List<ProductPurchaseResponse> productPurchaseResponses = productService.purchaseProducts(
                orderCreateRequest.orderProducts().stream()
                        .map(request -> new ProductPurchaseRequest(
                                request.productId(), request.quantity())
                        ).toList()
        );
        List<OrderProduct> orderProducts = orderProductRepository.saveAll(
                productPurchaseResponses.stream()
                        .map(response -> OrderProduct.create(
                                order, response.productId(), response.quantity(), response.name(), response.purchaseAmount()
                        )).toList()
        );

        List<OrderProductDto> orderProductDtos = orderProducts.stream()
                .map(OrderProductDto::from).toList();
        return new OrderCreateResponse(order.getId(), order.getMemberId(), order.getStatus(), orderProductDtos);
    }

    public OrderCancelResponse cancel(Long memberId, Long orderId) {
        Long checkedMemberId = memberService.findById(memberId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "order not found -> orderId: " + orderId
                ));
        order.cancel(checkedMemberId);

        List<OrderProduct> orderProducts = orderProductRepository.findAllByOrder(order);
        productService.stockRecovery(orderProducts.stream().map(
                orderProduct -> new ProductStockRecoveryRequest(
                        orderProduct.getProductId(), orderProduct.getQuantity()
                )
        ).toList());
        orderProductRepository.deleteAll(orderProducts);

        orderRepository.save(order);

        List<OrderProductDto> orderProductDtos = orderProducts.stream()
                .map(OrderProductDto::from).toList();
        return new OrderCancelResponse(order.getId(), order.getMemberId(), order.getStatus(), orderProductDtos);
    }

}
