package com.example.product.infrastructure;

import com.example.product.application.HoldStockRepository;
import com.example.product.domain.HoldStock;
import com.example.product.infrastructure.entity.HoldStockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HoldStockRepositoryImpl implements HoldStockRepository {

    private final HoldStockJpaRepository holdStockJpaRepository;

    @Override
    public List<HoldStock> findAllByProductId(Long productId) {
        return holdStockJpaRepository.findAllByProductId(productId)
                .stream().map(HoldStockEntity::toModel).toList();
    }

    @Override
    public void save(HoldStock holdStock) {
        holdStockJpaRepository.save(HoldStockEntity.from(holdStock));
    }

    @Override
    public void deleteAllByOrderId(Long orderId) {
        holdStockJpaRepository.deleteAllByOrderId(orderId);
    }

    @Override
    public List<HoldStock> findAllByOrderId(Long orderId) {
        return holdStockJpaRepository.findAllByOrderId(orderId)
                .stream().map(HoldStockEntity::toModel).toList();
    }

    @Override
    public List<HoldStock> findAll() {
        return holdStockJpaRepository.findAll()
                .stream().map(HoldStockEntity::toModel).toList();
    }

    @Override
    public void deleteAll() {
        holdStockJpaRepository.deleteAll();
    }
}
