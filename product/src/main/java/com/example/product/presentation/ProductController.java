package com.example.product.presentation;

import com.example.product.application.ProductService;
import com.example.product.common.dto.ProductDetails;
import com.example.product.common.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getProductList(
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        return productService.getProductList(cursor, size);
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
