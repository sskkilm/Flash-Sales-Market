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

    @PostMapping("/hold-stock")
    public StockHoldResponse holdStock(
            @Valid @RequestBody StockHoldRequest stockHoldRequest) {
        return productService.holdStock(stockHoldRequest);
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

    @PostMapping("{orderId}/apply/hold-stock")
    void applyHoldStock(@PathVariable Long orderId) {
        productService.applyHoldStock(orderId);
    }
}
