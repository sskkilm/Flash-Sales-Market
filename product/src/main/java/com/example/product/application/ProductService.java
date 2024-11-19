package com.example.product.application;

import com.example.product.aop.LockProducts;
import com.example.product.application.port.LocalDateTimeHolder;
import com.example.product.application.port.ProductRepository;
import com.example.product.domain.AmountCalculator;
import com.example.product.domain.PreoccupiedStock;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import com.example.product.exception.ProductServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static com.example.product.exception.error.ErrorCode.NOT_OPENED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockPreoccupationService stockPreoccupationService;
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

    @LockProducts
    @Transactional
    public StockPreoccupationResponse preoccupyStock(StockPreoccupationRequest stockPreoccupationRequest) {

        List<StockPreoccupationResult> stockPreoccupationResults = stockPreoccupationRequest.stockPreoccupationInfos().stream()
                .map(request -> {
                    Product product = productRepository.findById(request.productId());
                    if (product.isLimited() && product.isNotOpened()) {
                        throw new ProductServiceException(NOT_OPENED);
                    }

                    int preoccupiedStockQuantity = stockPreoccupationService.getPreoccupiedStockQuantityInProduct(product.getId());
                    log.info("{}번 상품에 선점된 재고: {}", product.getId(), preoccupiedStockQuantity);
                    product.checkOutOfStock(request.quantity(), preoccupiedStockQuantity);
                    stockPreoccupationService.preoccupy(stockPreoccupationRequest.orderId(), product.getId(), request.quantity());

                    BigDecimal amount = amountCalculator.calculate(product, request.quantity());

                    return new StockPreoccupationResult(product.getId(), product.getName(), request.quantity(), amount);
                }).toList();

        return new StockPreoccupationResponse(stockPreoccupationResults);
    }

    @LockProducts
    @Transactional
    public void applyPreoccupiedStock(Long orderId, List<Long> orderProductIds) {
        List<PreoccupiedStock> preoccupiedStocks = stockPreoccupationService.findAllByOrderId(orderId);
        preoccupiedStocks.forEach(holdStock -> {
            Product product = productRepository.findById(holdStock.getProductId());
            log.info("{}번 상품에 남은 재고: {}", product.getId(), product.getStockQuantity());
            product.decreaseStock(holdStock.getQuantity());
            productRepository.save(product);
        });
        stockPreoccupationService.release(orderId);
    }

    public void releasePreoccupiedStock(Long orderId) {
        stockPreoccupationService.release(orderId);
    }

}
