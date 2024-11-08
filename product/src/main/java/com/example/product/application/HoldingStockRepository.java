package com.example.product.application;

import com.example.product.domain.HoldingStock;

import java.util.List;

public interface HoldingStockRepository {
    List<HoldingStock> findAllByProductId(Long productId);

    void save(HoldingStock holdingStock);
}
