package com.port90.cartitem.infrastructure.repository.persistence;

import com.port90.cartitem.infrastructure.repository.persistence.entity.CartItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    List<CartItemEntity> findAllByMemberId(Long memberId);
}
