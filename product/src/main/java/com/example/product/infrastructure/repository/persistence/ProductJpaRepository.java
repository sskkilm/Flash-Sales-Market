package com.example.product.infrastructure.repository.persistence;

import com.example.product.infrastructure.repository.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findAllByIdIn(List<Long> productIds);
}
