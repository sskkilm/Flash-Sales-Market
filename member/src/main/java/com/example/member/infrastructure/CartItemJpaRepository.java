package com.example.member.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    CartItemEntity findByMemberIdAndProductId(Long memberId, Long productId);
}
