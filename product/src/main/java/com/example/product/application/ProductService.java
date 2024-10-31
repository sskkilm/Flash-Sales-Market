package com.example.product.application;

import com.example.product.domain.AmountCalculator;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import com.example.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AmountCalculator calculator = new AmountCalculator();

    public List<ProductDto> getProductList() {
        return productRepository.findAll()
                .stream().map(ProductDto::from).toList();
    }

    public ProductDetails getProductDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return ProductDetails.from(product);
    }

    public ProductPurchaseResponse purchase(ProductPurchaseRequest productPurchaseRequest) {
        List<PurchasedProductInfo> purchasedProductInfos = productPurchaseRequest.productInfos().stream().map(request -> {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ProductNotFoundException(request.productId()));

            product.decreaseStock(request.quantity());

            productRepository.save(product);

            BigDecimal calculatedAmount = calculator.calculateAmount(product, request.quantity());

            return new PurchasedProductInfo(product.getId(), product.getName(), request.quantity(), calculatedAmount);
        }).toList();

        return new ProductPurchaseResponse(purchasedProductInfos);
    }

    public void restoreStock(ProductRestoreStockRequest productRestoreStockRequest) {
        List<ProductRestoreStockInfo> productRestoreStockInfos = productRestoreStockRequest.productRestoreStockInfos();
        productRestoreStockInfos.forEach(request -> {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new ProductNotFoundException(request.productId()));

            product.increaseStock(request.quantity());

            productRepository.save(product);
        });
    }

    public ProductDto findById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return ProductDto.from(product);
    }

}
