package com.example.member.infrastructure;

import com.example.member.application.MemberRepository;
import com.example.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id)
                .map(MemberEntity::toModel);
    }
}
