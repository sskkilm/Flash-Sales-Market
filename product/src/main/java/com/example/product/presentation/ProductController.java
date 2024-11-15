package com.example.product.presentation;

import com.example.product.application.ProductService;
import com.example.product.dto.ProductDetails;
import com.example.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProductList() {
        return productService.getProductList();
    }

    @GetMapping("/{productId}")
    public ProductDetails<?> getProductDetails(
            @PathVariable Long productId
    ) {
        return productService.getProductDetails(productId);
    }

    @GetMapping("/{productId}/stock")
    public int getStockQuantity(
            @PathVariable Long productId
    ) {
        return productService.getStockQuantity(productId);
    }

}
