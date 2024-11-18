package com.example.product.infrastructure.repository;

import com.example.product.infrastructure.entity.PreoccupiedStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPreoccupationJpaRepository extends JpaRepository<PreoccupiedStockEntity, Long> {

    List<PreoccupiedStockEntity> findAllByProductId(Long productId);

    void deleteAllByOrderId(Long orderId);

    List<PreoccupiedStockEntity> findAllByOrderId(Long orderId);
}
