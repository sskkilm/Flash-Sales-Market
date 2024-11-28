package com.example.product.infrastructure.repository.persistence.mapper;

import com.example.product.domain.Product;
import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final List<ProductMapHandler> handlers;

    public ProductEntity toEntity(Product product) {
        for (ProductMapHandler handler : handlers) {
            if (handler.support(product)) {
                return handler.handle(product);
            }
        }
        throw new IllegalArgumentException("Product not supported");
    }

}
