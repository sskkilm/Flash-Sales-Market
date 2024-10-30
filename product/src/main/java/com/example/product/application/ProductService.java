package com.example.product.application;

import com.example.product.domain.AmountCalculator;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import com.example.product.exception.ProductNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final AmountCalculator calculator;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.calculator = new AmountCalculator();
    }

    public List<ProductResponse> getProductList() {
        return productRepository.findAll()
                .stream().map(ProductResponse::from).toList();
    }

    public ProductDetails getProductDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductDetails.from(product);
    }

    public List<ProductPurchaseFeignResponse> purchase(List<ProductPurchaseFeignRequest> productPurchaseFeignRequests) {
        return productPurchaseFeignRequests.stream().map(request -> {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ProductNotFoundException(request.productId()));

            product.decreaseStock(request.quantity());

            productRepository.save(product);

            BigDecimal calculatedAmount = calculator.calculateAmount(product, request.quantity());

            return new ProductPurchaseFeignResponse(product.getId(), product.getName(), request.quantity(), calculatedAmount);
        }).toList();
    }

    public void restoreStock(List<ProductRestoreStockFeignRequest> list) {
        list.forEach(request -> {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ProductNotFoundException(request.productId()));

            product.increaseStock(request.quantity());

            productRepository.save(product);
        });
    }

    public ProductFeignResponse findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return new ProductFeignResponse(product.getId(), product.getName(), product.getPrice());
    }

}
