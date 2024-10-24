package com.example.app;

import com.example.member.application.CartItemService;
import com.example.member.dto.CartItemCreateRequest;
import com.example.member.dto.CartItemDto;
import com.example.member.dto.CartItemUpdateRequest;
import com.example.order.application.OrderService;
import com.example.order.dto.*;
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
    private final CartItemService cartItemService;

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
    public OrderReturnResponse returns(
            @PathVariable Long memberId,
            @PathVariable Long orderId
    ) {
        return orderService.returns(memberId, orderId);
    }

    @GetMapping("/orders/{memberId}")
    public List<OrderHistory> getOrderHistory(
            @PathVariable Long memberId
    ) {
        return orderService.getOrderHistory(memberId);
    }

    @PostMapping("/cart-items/{memberId}")
    public void create(
            @PathVariable Long memberId,
            @RequestBody @Valid CartItemCreateRequest request
    ) {
        cartItemService.create(memberId, request);
    }

    @PatchMapping("/cart-items/{memberId}/{cartItemId}")
    public void update(
            @PathVariable Long memberId,
            @PathVariable Long cartItemId,
            @RequestBody @Valid CartItemUpdateRequest request
    ) {
        cartItemService.update(memberId, cartItemId, request);
    }

    @DeleteMapping("/cart-items/{memberId}/{cartItemId}")
    public void delete(
            @PathVariable Long memberId,
            @PathVariable Long cartItemId
    ) {
        cartItemService.delete(memberId, cartItemId);
    }

    @GetMapping("/cart-items/{memberId}")
    public List<CartItemDto> getCartItemList(
            @PathVariable Long memberId
    ) {
        return cartItemService.getCartItemList(memberId);
    }
}
