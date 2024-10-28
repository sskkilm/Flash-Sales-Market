package com.example.product.api;

import com.example.product.application.ProductService;
import com.example.product.dto.ProductFeignResponse;
import com.example.product.dto.ProductPurchaseFeignRequest;
import com.example.product.dto.ProductPurchaseFeignResponse;
import com.example.product.dto.ProductRestoreStockFeignRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/internal")
public class ProductInternalController {

    private final ProductService productService;

    @PostMapping("/purchase")
    public List<ProductPurchaseFeignResponse> purchase(
            @Valid @RequestBody List<ProductPurchaseFeignRequest> productPurchaseFeignRequests) {
        return productService.purchase(productPurchaseFeignRequests);
    }

    @PostMapping("/restore-stock")
    public void restockStock(
            @Valid @RequestBody List<ProductRestoreStockFeignRequest> productRestoreStockFeignRequests) {
        productService.restockStock(productRestoreStockFeignRequests);
    }

    @GetMapping("/{productId}")
    public ProductFeignResponse findById(@PathVariable Long productId) {
        return productService.findById(productId);
    }
}
