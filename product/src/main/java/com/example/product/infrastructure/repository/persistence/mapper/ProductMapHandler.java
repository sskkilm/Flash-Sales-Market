package com.example.product.infrastructure.repository.persistence.mapper;

import com.example.product.domain.Product;
import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;

public interface ProductMapHandler {

    boolean support(Product product);

    ProductEntity handle(Product product);

}
