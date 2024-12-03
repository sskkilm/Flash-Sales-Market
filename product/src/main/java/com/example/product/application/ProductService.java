package com.example.product.application;

import com.example.product.application.port.LocalDateTimeProvider;
import com.example.product.application.port.ProductRepository;
import com.example.product.application.port.StockCacheRepository;
import com.example.product.common.aop.DistributedLock;
import com.example.product.common.dto.ProductDetails;
import com.example.product.common.dto.ProductDto;
import com.example.product.common.dto.request.StockDecreaseRequest;
import com.example.product.common.dto.request.StockIncreaseRequest;
import com.example.product.domain.EventProduct;
import com.example.product.domain.Product;
import com.example.product.domain.exception.ProductServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.product.domain.ProductType.EVENT;
import static com.example.product.domain.exception.ErrorCode.CONTAINS_NOT_EXISTED_PRODUCT;
import static com.example.product.domain.exception.ErrorCode.CONTAINS_NOT_OPENED_EVENT_PRODUCT;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocalDateTimeProvider localDateTimeProvider;
    private final StockCacheRepository stockCacheRepository;

    public List<ProductDto> getProductList(Long cursor, int size) {
        return productRepository.findAllSellableProduct(cursor, size, localDateTimeProvider.now())
                .stream().map(ProductDto::from).toList();
    }

    public ProductDetails<?> getProductDetails(Long id) {
        Product product = productRepository.findById(id);
        return ProductDetails.of(product);
    }

    @Cacheable(value = "products", key = "#productId + ':stock'")
    public int getStockQuantity(Long productId) {
        return productRepository.findStockQuantityById(productId);
    }

    public ProductDto getProductInfo(Long productId) {
        Product product = productRepository.findById(productId);

        return ProductDto.from(product);
    }

    @DistributedLock
    @Transactional
    public void decreaseStock(List<StockDecreaseRequest> requests) {
        List<Long> productIds = requests
                .stream()
                .map(StockDecreaseRequest::productId)
                .toList();
        Map<Long, Product> productMap = productRepository.findAllByIdIn(productIds)
                .stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
        requests
                .forEach(request -> {
                    Product product = productMap.get(request.productId());
                    product.decreaseStock(request.quantity());
                    productRepository.save(product);
                    log.info("{}번 상품 재고 {}개 감소", request.productId(), request.quantity());
                });
    }

    @DistributedLock
    @Transactional
    public void increaseStock(List<StockIncreaseRequest> requests) {
        requests
                .forEach(request -> {
                    Product product = productRepository.findById(request.productId());
                    product.increaseStock(request.quantity());
                    productRepository.save(product);
                    log.info("{}번 상품 재고 {}개 증가", request.productId(), request.quantity());
                });
    }

    public List<ProductDto> getProductInfos(List<Long> productIds) {
        List<Product> products = productRepository.findAllByIdIn(productIds);

        validateIdContains(productIds, products);
        validateNotOpenedEventProductContains(products);

        cacheStock(products);

        return products
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    private static void validateIdContains(List<Long> productIds, List<Product> products) {
        Set<Long> productIdSet = products
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
        for (Long productId : productIds) {
            if (!productIdSet.contains(productId)) {
                throw new ProductServiceException(CONTAINS_NOT_EXISTED_PRODUCT);
            }
        }
    }

    private void validateNotOpenedEventProductContains(List<Product> products) {
        products
                .forEach(
                        product -> {
                            if (product.getType() == EVENT
                                    && ((EventProduct) product).getOpenTime().isAfter(localDateTimeProvider.now())) {
                                throw new ProductServiceException(CONTAINS_NOT_OPENED_EVENT_PRODUCT);
                            }
                        }
                );
    }

    private void cacheStock(List<Product> products) {
        products
                .forEach(
                        product -> stockCacheRepository.cache(
                                product.getId(), product.getStockQuantity()
                        )
                );
    }
}
