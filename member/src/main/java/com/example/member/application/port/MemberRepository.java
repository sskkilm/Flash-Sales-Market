package com.example.member.application.port;

import com.example.member.domain.model.Member;

public interface MemberRepository {

    Member save(Member member);

    boolean existsByUsername(String username);

    Member findByUsername(String username);

    Member findById(Long memberId);
}
