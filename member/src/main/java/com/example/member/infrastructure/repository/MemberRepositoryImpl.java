package com.example.member.infrastructure.repository;

import com.example.member.application.port.MemberRepository;
import com.example.member.domain.exception.MemberServiceException;
import com.example.member.domain.model.Member;
import com.example.member.infrastructure.repository.persistence.MemberJpaRepository;
import com.example.member.infrastructure.repository.persistence.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.member.domain.exception.ErrorCode.MEMBER_NOT_FOUND;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return MemberMapper.toModel(
                memberJpaRepository.save(
                        MemberMapper.toEntity(member)
                )
        );
    }

    @Override
    public boolean existsByUsername(String username) {
        return memberJpaRepository.existsByUsername(username);
    }

    @Override
    public Member findByUsername(String username) {
        return memberJpaRepository.findByUsername(username)
                .map(MemberMapper::toModel)
                .orElseThrow(() -> new MemberServiceException(MEMBER_NOT_FOUND));
    }

    @Override
    public Member findById(Long memberId) {
        return memberJpaRepository.findById(memberId)
                .map(MemberMapper::toModel)
                .orElseThrow(() -> new MemberServiceException(MEMBER_NOT_FOUND));
    }
}
