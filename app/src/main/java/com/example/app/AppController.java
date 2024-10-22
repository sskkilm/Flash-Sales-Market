package com.example.app;

import com.example.order.application.OrderService;
import com.example.order.dto.OrderCancelResponse;
import com.example.order.dto.OrderCreateRequest;
import com.example.order.dto.OrderCreateResponse;
import com.example.product.application.ProductService;
import com.example.product.dto.ProductDetails;
import com.example.product.dto.ProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final ProductService productService;
    private final OrderService orderService;

    @GetMapping("/products")
    public List<ProductDto> getProductList() {
        return productService.getProductList();
    }

    @GetMapping("/products/{productId}")
    public ProductDetails getProductDetails(
            @PathVariable Long productId
    ) {
        return productService.getProductDetails(productId);
    }

    @PostMapping("/orders/{memberId}")
    public OrderCreateResponse create(
            @PathVariable Long memberId,
            @RequestBody @Valid OrderCreateRequest orderCreateRequest
    ) {
        return orderService.create(memberId, orderCreateRequest);
    }

    @PostMapping("/orders/{memberId}/{orderId}/cancel")
    public OrderCancelResponse cancel(
            @PathVariable Long memberId,
            @PathVariable Long orderId
    ) {
        return orderService.cancel(memberId, orderId);
    }

    @PostMapping("/orders/{memberId}/{orderId}/return")
    public void returns(
            @PathVariable Long memberId,
            @PathVariable Long orderId
    ) {
        orderService.returns(memberId, orderId);
    }
}
