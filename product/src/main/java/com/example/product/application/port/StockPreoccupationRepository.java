package com.example.product.application.port;

import com.example.product.domain.PreoccupiedStock;

import java.util.List;

public interface StockPreoccupationRepository {
    List<Integer> findQuantitiesByProductId(Long productId);

    void save(PreoccupiedStock preoccupiedStock);

    void deleteAllByOrderId(Long orderId);

    List<PreoccupiedStock> findAllByOrderId(Long orderId);

    List<PreoccupiedStock> findAll();

    void deleteAll();

    void saveAll(List<PreoccupiedStock> preoccupiedStocks);
}
