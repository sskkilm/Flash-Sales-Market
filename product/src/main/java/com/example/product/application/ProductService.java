package com.example.product.application;

import com.example.product.domain.AmountCalculator;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocalDateTimeHolder localDateTimeHolder;
    private final AmountCalculator amountCalculator = new AmountCalculator();

    public List<ProductDto> getProductList() {
        return productRepository.findAllSellableProduct(localDateTimeHolder.now())
                .stream().map(ProductDto::from).toList();
    }

    public ProductDetails<?> getProductDetails(Long id) {
        Product product = productRepository.findById(id);
        return ProductDetails.of(product);
    }

    public ProductOrderResponse getProductOrderInfo(ProductOrderRequest productOrderRequest) {
        List<OrderedProductInfo> orderedProductInfos = productOrderRequest.productOrderInfos().stream()
                .map(request -> {
                    Product product = productRepository.findById(request.productId());

                    product.checkOutOfStock(request.quantity());

                    BigDecimal amount = amountCalculator.calculate(product, request.quantity());

                    return new OrderedProductInfo(product.getId(), product.getName(), request.quantity(), amount);
                }).toList();

        return new ProductOrderResponse(orderedProductInfos);
    }

    public void restock(ProductRestockRequest productRestockRequest) {
        List<ProductRestockInfo> productRestockInfos = productRestockRequest.productRestockInfos();
        productRestockInfos.forEach(request -> {
            Product product = productRepository.findById(request.productId());

            product.increaseStock(request.quantity());

            productRepository.save(product);
        });
    }

    public ProductDto findById(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductDto.from(product);
    }

    public int getStockQuantity(Long productId) {
        Product product = productRepository.findById(productId);
        return product.getStockQuantity();
    }

    @Transactional
    public void decreaseStock(List<OrderCompletedProductDto> orderCompletedProducts) {
        orderCompletedProducts
                .forEach(orderCompletedProduct -> {
                    Product product = productRepository.findById(orderCompletedProduct.productId());
                    product.decreaseStock(orderCompletedProduct.quantity());
                    productRepository.save(product);
                });
    }
}
