package com.example.product.api;

import com.example.product.application.ProductService;
import com.example.product.dto.ProductDto;
import com.example.product.dto.ProductPurchaseRequest;
import com.example.product.dto.ProductPurchaseResponse;
import com.example.product.dto.ProductRestoreStockRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/internal")
public class ProductInternalController {

    private final ProductService productService;

    @PostMapping("/purchase")
    public ProductPurchaseResponse purchase(
            @Valid @RequestBody ProductPurchaseRequest productPurchaseRequest) {
        return productService.purchase(productPurchaseRequest);
    }

    @PostMapping("/restore-stock")
    public void restoreStock(
            @Valid @RequestBody ProductRestoreStockRequest productRestoreStockRequest) {
        productService.restoreStock(productRestoreStockRequest);
    }

    @GetMapping("/{productId}")
    public ProductDto findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }
}
