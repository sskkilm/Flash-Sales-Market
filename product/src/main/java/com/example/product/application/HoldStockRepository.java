package com.example.product.application;

import com.example.product.domain.HoldStock;

import java.util.List;

public interface HoldStockRepository {
    List<HoldStock> findAllByProductId(Long productId);

    void save(HoldStock holdStock);

    void deleteAllByOrderId(Long orderId);

    List<HoldStock> findAllByOrderId(Long orderId);

    List<HoldStock> findAll();

    void deleteAll();
}
