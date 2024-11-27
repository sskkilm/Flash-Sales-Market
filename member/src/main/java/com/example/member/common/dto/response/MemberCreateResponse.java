package com.example.member.common.dto.response;

import com.example.member.domain.model.Member;

public record MemberCreateResponse(
    Long memberId,
    String username,
    String password
) {
    public static MemberCreateResponse from(Member member) {
        return new MemberCreateResponse(
                member.getId(),
                member.getUsername(),
                member.getPassword()
        );
    }
}
