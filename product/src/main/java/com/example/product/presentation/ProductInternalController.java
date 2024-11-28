package com.example.product.presentation;

import com.example.product.application.ProductService;
import com.example.product.common.dto.ProductDto;
import com.example.product.common.dto.request.StockIncreaseRequest;
import com.example.product.common.dto.request.StockDecreaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products/internal")
public class ProductInternalController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ProductDto getProductInfo(@PathVariable Long productId) {
        return productService.getProductInfo(productId);
    }

    @PostMapping("/increase/stock")
    public void increaseStock(
            @RequestBody List<StockIncreaseRequest> stockIncreaseRequests
    ) {
        productService.increaseStock(stockIncreaseRequests);
    }

    @PostMapping("/decrease/stock")
    public void decreaseStock(@RequestBody List<StockDecreaseRequest> stockDecreaseRequests) {
        productService.decreaseStock(stockDecreaseRequests);
    }

}
