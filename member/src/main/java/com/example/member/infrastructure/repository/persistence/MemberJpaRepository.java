package com.example.member.infrastructure.repository.persistence;

import com.example.member.infrastructure.repository.persistence.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByUsername(String username);

    Optional<MemberEntity> findByUsername(String username);
}
