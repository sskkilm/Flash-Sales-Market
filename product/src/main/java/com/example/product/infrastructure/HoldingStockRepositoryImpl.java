package com.example.product.infrastructure;

import com.example.product.application.HoldingStockRepository;
import com.example.product.domain.HoldingStock;
import com.example.product.infrastructure.entity.HoldingStockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HoldingStockRepositoryImpl implements HoldingStockRepository {

    private final HoldingStockJpaRepository holdingStockJpaRepository;

    @Override
    public List<HoldingStock> findAllByProductId(Long productId) {
        return holdingStockJpaRepository.findAllByProductId(productId)
                .stream().map(HoldingStockEntity::toModel).toList();
    }

    @Override
    public void save(HoldingStock holdingStock) {
        holdingStockJpaRepository.save(HoldingStockEntity.from(holdingStock));
    }

    @Override
    public void deleteAllByOrderId(Long orderId) {
        holdingStockJpaRepository.deleteAllByOrderId(orderId);
    }

    @Override
    public List<HoldingStock> findAllByOrderId(Long orderId) {
        return holdingStockJpaRepository.findAllByOrderId(orderId)
                .stream().map(HoldingStockEntity::toModel).toList();
    }
}
