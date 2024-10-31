package com.example.member.application;

import com.example.member.domain.Member;

public interface MemberRepository {

    Member save(Member member);
}
