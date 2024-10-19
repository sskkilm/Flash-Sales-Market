package com.example.member.application;

import com.example.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {
    Optional<Member> findById(Long id);
}
