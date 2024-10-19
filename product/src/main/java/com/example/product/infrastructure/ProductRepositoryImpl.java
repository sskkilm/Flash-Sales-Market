package com.example.product.infrastructure;

import com.example.product.application.ProductRepository;
import com.example.product.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll()
                .stream().map(this::convertToModel).toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(this::convertToModel);
    }

    private Product convertToModel(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }
        return productEntity.toModel();
    }
}
