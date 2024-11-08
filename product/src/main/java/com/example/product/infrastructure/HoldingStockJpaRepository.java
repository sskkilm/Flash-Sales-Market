package com.example.product.infrastructure;

import com.example.product.infrastructure.entity.HoldingStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoldingStockJpaRepository extends JpaRepository<HoldingStockEntity, Long> {

    List<HoldingStockEntity> findAllByProductId(Long productId);
}
