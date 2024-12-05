package com.example.product.application.port;

import com.example.product.domain.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductRepository {

    Product findById(Long id);

    Product save(Product product);

    List<Product> findAllSellableProduct(Long cursor, int size, LocalDateTime now);

    void deleteAll();

    List<Product> findAllByIdIn(List<Long> productIds);

    int findStockQuantityById(Long productId);

    void saveAll(List<Product> products);
}
