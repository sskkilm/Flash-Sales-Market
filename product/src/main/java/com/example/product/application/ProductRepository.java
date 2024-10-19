package com.example.product.application;

import com.example.product.domain.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();
}
