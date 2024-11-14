package com.example.product.infrastructure;

import com.example.product.infrastructure.entity.HoldStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoldStockJpaRepository extends JpaRepository<HoldStockEntity, Long> {

    List<HoldStockEntity> findAllByProductId(Long productId);

    void deleteAllByOrderId(Long orderId);

    List<HoldStockEntity> findAllByOrderId(Long orderId);
}
