package com.example.product.infrastructure;

import com.example.product.application.ProductRepository;
import com.example.product.domain.Product;
import com.example.product.exception.ProductServiceException;
import com.example.product.infrastructure.entity.ProductEntity;
import com.example.product.infrastructure.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.product.exception.error.ErrorCode.PRODUCT_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductMapper productMapper;

    @Override
    public Product findById(Long id) {
        return productJpaRepository.findById(id).map(ProductEntity::toModel)
                .orElseThrow(() -> new ProductServiceException(PRODUCT_NOT_FOUND));
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(productMapper.toEntity(product)).toModel();
    }

    @Override
    public List<Product> findAllSellableProduct(LocalDateTime now) {
        return productQueryRepository.findAllSellableProduct(now)
                .stream().map(ProductEntity::toModel).toList();
    }

    @Override
    public void deleteAll() {
        productJpaRepository.deleteAll();
    }

}
