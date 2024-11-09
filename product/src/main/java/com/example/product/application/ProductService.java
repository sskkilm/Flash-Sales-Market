package com.example.product.application;

import com.example.product.domain.AmountCalculator;
import com.example.product.domain.HoldingStock;
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
    private final HoldingStockService holdingStockService;
    private final AmountCalculator amountCalculator = new AmountCalculator();

    public List<ProductDto> getProductList() {
        return productRepository.findAllSellableProduct(localDateTimeHolder.now())
                .stream().map(ProductDto::from).toList();
    }

    public ProductDetails<?> getProductDetails(Long id) {
        Product product = productRepository.findById(id);
        return ProductDetails.of(product);
    }

    @Transactional
    public ProductOrderResponse order(ProductOrderRequest productOrderRequest) {
        List<OrderedProductInfo> orderedProductInfos = productOrderRequest.productOrderInfos().stream()
                .map(request -> {
                    Product product = productRepository.findById(request.productId());

                    int holdingStockQuantity = holdingStockService.getHoldingStockQuantityInProduct(product.getId());
                    product.checkOutOfStock(request.quantity(), holdingStockQuantity);

                    holdingStockService.create(productOrderRequest.orderId(), product.getId(), request.quantity());

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

    public void releaseHoldingStock(Long orderId) {
        holdingStockService.release(orderId);
    }

    @Transactional
    public void applyHoldingStock(Long orderId) {
        List<HoldingStock> holdingStocks = holdingStockService.findAllByOrderId(orderId);
        holdingStocks.forEach(holdingStock -> {
            Product product = productRepository.findById(holdingStock.getProductId());
            product.decreaseStock(holdingStock.getQuantity());
            productRepository.save(product);
        });
        holdingStockService.release(orderId);
    }
}
