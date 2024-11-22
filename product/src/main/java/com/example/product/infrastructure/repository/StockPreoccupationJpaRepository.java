package com.example.product.infrastructure.repository;

import com.example.product.infrastructure.entity.PreoccupiedStockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPreoccupationJpaRepository extends JpaRepository<PreoccupiedStockEntity, Long> {

    @Query("select ps.quantity from PreoccupiedStock ps where ps.productId = :productId")
    List<Integer> findQuantitiesByProductId(@Param("productId") Long productId);

    void deleteAllByOrderId(Long orderId);

    List<PreoccupiedStockEntity> findAllByOrderId(Long orderId);
}
