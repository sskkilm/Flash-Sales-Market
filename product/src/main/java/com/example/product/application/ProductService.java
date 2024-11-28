package com.example.product.application;

import com.example.product.application.port.LocalDateTimeProvider;
import com.example.product.application.port.ProductRepository;
import com.example.product.common.aop.DistributedLock;
import com.example.product.common.dto.ProductDetails;
import com.example.product.common.dto.ProductDto;
import com.example.product.common.dto.request.StockIncreaseRequest;
import com.example.product.common.dto.request.StockDecreaseRequest;
import com.example.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final LocalDateTimeProvider localDateTimeProvider;

    public List<ProductDto> getProductList() {
        return productRepository.findAllSellableProduct(localDateTimeProvider.now())
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

    public int getStockQuantity(Long productId) {
        Product product = productRepository.findById(productId);
        return product.getStockQuantity();
    }

    @DistributedLock
    @Transactional
    public void decreaseStock(List<StockDecreaseRequest> requests) {
        requests
                .forEach(request -> {
                    Product product = productRepository.findById(request.productId());
                    product.decreaseStock(request.quantity());
                    productRepository.save(product);
                    log.info("{}번 상품 재고 {}개 감소", request.productId(), request.quantity());
                });
    }

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

}
