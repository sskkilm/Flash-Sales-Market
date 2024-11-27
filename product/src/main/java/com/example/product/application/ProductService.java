package com.example.product.application;

import com.example.product.aop.DistributedLock;
import com.example.product.application.port.LocalDateTimeHolder;
import com.example.product.application.port.ProductRepository;
import com.example.product.domain.PreoccupiedStock;
import com.example.product.domain.Product;
import com.example.product.dto.*;
import com.example.product.exception.ProductServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.product.exception.error.ErrorCode.PRODUCT_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockPreoccupationService stockPreoccupationService;
    private final LocalDateTimeHolder localDateTimeHolder;

    public List<ProductDto> getProductList() {
        return productRepository.findAllSellableProduct(localDateTimeHolder.now())
                .stream().map(ProductDto::from).toList();
    }

    public ProductDto getProductInfo(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductDto.from(product);
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

    public StockPreoccupationResponse preoccupyStock(StockPreoccupationRequest request) {

        List<Long> productIds = request.stockPreoccupationInfos().stream()
                .map(StockPreoccupationInfo::productId)
                .toList();

        Map<Long, Product> productMap = productRepository.findAllByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        validateProductIdInfo(productIds, productMap);

        List<StockPreoccupationResult> results = stockPreoccupationService.preoccupy(
                request.orderId(), productMap, request.stockPreoccupationInfos()
        );

        log.info("Preoccupied Stock In Product Ids:{}", productIds);

        return new StockPreoccupationResponse(results);
    }

    @DistributedLock
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

    private static void validateProductIdInfo(List<Long> productIds, Map<Long, Product> productMap) {
        for (Long productId : productIds) {
            if (!productMap.containsKey(productId)) {
                throw new ProductServiceException(PRODUCT_NOT_FOUND);
            }
        }
    }

}
