package com.example.product.application;

import com.example.common.domain.Money;
import com.example.common.dto.ProductPurchaseRequest;
import com.example.common.dto.ProductPurchaseResponse;
import com.example.product.domain.AmountCalculator;
import com.example.product.domain.Product;
import com.example.product.dto.ProductDetails;
import com.example.product.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final AmountCalculator calculator;

    public List<ProductDto> getProductList() {
        return productRepository.findAll()
                .stream().map(ProductDto::from).toList();
    }

    public ProductDetails getProductDetails(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "product not found -> productId: " + id)
                );
        return ProductDetails.from(product);
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> productPurchaseRequests) {
        return productPurchaseRequests.stream().map(request -> {
            Product product = productRepository.findById(request.productId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "product not found -> productId: " + request.productId())
                    );
            product.decreaseStock(request.quantity());
            productRepository.save(product);

            Money calculatedAmount = calculator.calculateAmount(product, request.quantity());

            return new ProductPurchaseResponse(product.getId(), request.quantity(), calculatedAmount);
        }).toList();
    }
}
