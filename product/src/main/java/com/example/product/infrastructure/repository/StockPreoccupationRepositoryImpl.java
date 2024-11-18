package com.example.product.infrastructure.repository;

import com.example.product.application.port.StockPreoccupationRepository;
import com.example.product.domain.PreoccupiedStock;
import com.example.product.infrastructure.entity.PreoccupiedStockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StockPreoccupationRepositoryImpl implements StockPreoccupationRepository {

    private final StockPreoccupationJpaRepository stockPreoccupationJpaRepository;

    @Override
    public List<PreoccupiedStock> findAllByProductId(Long productId) {
        return stockPreoccupationJpaRepository.findAllByProductId(productId)
                .stream().map(PreoccupiedStockEntity::toModel).toList();
    }

    @Override
    public void save(PreoccupiedStock preoccupiedStock) {
        stockPreoccupationJpaRepository.save(PreoccupiedStockEntity.from(preoccupiedStock));
    }

    @Override
    public void deleteAllByOrderId(Long orderId) {
        stockPreoccupationJpaRepository.deleteAllByOrderId(orderId);
    }

    @Override
    public List<PreoccupiedStock> findAllByOrderId(Long orderId) {
        return stockPreoccupationJpaRepository.findAllByOrderId(orderId)
                .stream().map(PreoccupiedStockEntity::toModel).toList();
    }

    @Override
    public List<PreoccupiedStock> findAll() {
        return stockPreoccupationJpaRepository.findAll()
                .stream().map(PreoccupiedStockEntity::toModel).toList();
    }

    @Override
    public void deleteAll() {
        stockPreoccupationJpaRepository.deleteAll();
    }
}
