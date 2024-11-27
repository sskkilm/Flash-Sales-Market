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

    @PostMapping("/restock-stock")
    public void restock(
            @Valid @RequestBody ProductRestockRequest productRestockRequest) {
        productService.restock(productRestockRequest);
    }

    @GetMapping("/{productId}")
    public ProductDto getProductInfo(@PathVariable Long productId) {
        return productService.getProductInfo(productId);
    }

    @PostMapping("/decrease/stock")
    public void decreaseStock(@RequestBody List<OrderCompletedProductDto> orderCompletedProducts) {
        productService.decreaseStock(orderCompletedProducts);
    }

    @PostMapping("/preoccupation")
    public StockPreoccupationResponse preoccupyStock(
            @Valid @RequestBody StockPreoccupationRequest stockPreoccupationRequest) {
        return productService.preoccupyStock(stockPreoccupationRequest);
    }

    @PostMapping("/{orderId}/preoccupation/release")
    public void releasePreoccupiedStock(@PathVariable Long orderId) {
        productService.releasePreoccupiedStock(orderId);
    }

}
