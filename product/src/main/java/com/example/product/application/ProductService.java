package com.example.product.application;

import com.example.product.aop.LockProducts;
import com.example.product.aop.LockProductsInOrder;
import com.example.product.domain.AmountCalculator;
import com.example.product.domain.HoldStock;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final HoldStockService holdStockService;
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

    @Transactional
    @LockProducts
    public StockHoldResponse holdStock(StockHoldRequest stockHoldRequest) {

        List<StockHoldResult> stockHoldResults = stockHoldRequest.stockHoldInfos().stream()
                .map(request -> {
                    Product product = productRepository.findById(request.productId());

                    int holdStockQuantity = holdStockService.getHoldStockQuantityInProduct(product.getId());
                    log.info("{}번 상품에 선점된 재고: {}", product.getId(), holdStockQuantity);
                    product.checkOutOfStock(request.quantity(), holdStockQuantity);
                    holdStockService.hold(stockHoldRequest.orderId(), product.getId(), request.quantity());

                    BigDecimal amount = amountCalculator.calculate(product, request.quantity());

                    return new StockHoldResult(product.getId(), product.getName(), request.quantity(), amount);
                }).toList();

        return new StockHoldResponse(stockHoldResults);
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
        holdStockService.release(orderId);
    }

    @Transactional
    @LockProductsInOrder
    public void applyHoldStock(Long orderId) {
        List<HoldStock> holdStocks = holdStockService.findAllHoldStockByOrderId(orderId);
        holdStocks.forEach(holdStock -> {
            Product product = productRepository.findById(holdStock.getProductId());
            product.decreaseStock(holdStock.getQuantity());
            log.info("{}번 상품에 남은 재고: {}", product.getId(), product.getStockQuantity());
            productRepository.save(product);
        });
        holdStockService.release(orderId);
    }
}
