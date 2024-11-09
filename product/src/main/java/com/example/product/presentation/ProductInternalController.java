package com.example.product.presentation;

import com.example.product.application.ProductService;
import com.example.product.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/internal")
public class ProductInternalController {

    private final ProductService productService;

    @PostMapping("/order")
    public ProductOrderResponse order(
            @Valid @RequestBody ProductOrderRequest productOrderRequest) {
        return productService.order(productOrderRequest);
    }

    @PostMapping("/restock-stock")
    public void restock(
            @Valid @RequestBody ProductRestockRequest productRestockRequest) {
        productService.restock(productRestockRequest);
    }

    @GetMapping("/{productId}")
    public ProductDto findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }

    @PostMapping("/decrease/stock")
    public void decreaseStock(@RequestBody List<OrderCompletedProductDto> orderCompletedProducts) {
        productService.decreaseStock(orderCompletedProducts);
    }

    @PostMapping("/{orderId}/release/holding-stock")
    public void releaseHoldingStock(@PathVariable Long orderId) {
        productService.releaseHoldingStock(orderId);
    }

    @PostMapping("{orderId}/apply/holding-stock")
    void extractHoldingStock(@PathVariable Long orderId) {
        productService.applyHoldingStock(orderId);
    }
}
