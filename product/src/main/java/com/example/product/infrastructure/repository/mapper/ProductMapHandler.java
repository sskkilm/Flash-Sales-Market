package com.example.product.infrastructure.repository.mapper;

import com.example.product.domain.Product;
import com.example.product.infrastructure.entity.ProductEntity;

public interface ProductMapHandler {

    boolean support(Product product);

    ProductEntity handle(Product product);

}
