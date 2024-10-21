package com.example.app;

import com.example.member.application.MemberService;
import com.example.order.application.OrderService;
import com.example.order.dto.OrderCreateRequest;
import com.example.product.application.ProductService;
import com.example.product.dto.ProductDetails;
import com.example.product.dto.ProductDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AppController {

    private final ProductService productService;
    private final OrderService orderService;
    private final MemberService memberService;

    @GetMapping("/products")
    public List<ProductDto> getProductList() {
        return productService.getProductList();
    }

    @GetMapping("/products/{productId}")
    public ProductDetails getProductDetails(
            @PathVariable Long productId
    ) {
        return productService.getProductDetails(productId);
    }

    @PostMapping("/orders/{memberId}")
    public void order(
            @PathVariable Long memberId,
            @RequestBody @Valid OrderCreateRequest orderCreateRequest
    ) {
        orderService.order(memberId, orderCreateRequest);
    }
}
