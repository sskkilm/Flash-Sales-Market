package com.example.product.api;

import com.example.product.application.ProductService;
import com.example.product.dto.ProductDto;
import com.example.product.dto.ProductOrderRequest;
import com.example.product.dto.ProductOrderResponse;
import com.example.product.dto.ProductRestockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
