package com.example.dropsaleplatform;

import com.example.product.application.ProductService;
import com.example.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final ProductService productService;

    @GetMapping("/products")
    public List<ProductDto> getProductList() {
        return productService.getProductList();
    }

}
