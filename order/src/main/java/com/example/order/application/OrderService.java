package com.example.order.application;

import com.example.common.dto.ProductPurchaseRequest;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.member.application.MemberService;
import com.example.order.application.repository.OrderProductRepository;
import com.example.order.application.repository.OrderRepository;
import com.example.order.domain.Order;
import com.example.order.domain.OrderProduct;
import com.example.order.dto.OrderCreateRequest;
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
    public void order(Long memberId, OrderCreateRequest orderCreateRequest) {
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
        orderProductRepository.saveAll(orderProducts);
    }
}
