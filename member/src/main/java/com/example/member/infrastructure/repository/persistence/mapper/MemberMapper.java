package com.example.member.infrastructure.repository.persistence.mapper;

import com.example.member.domain.model.Member;
import com.example.member.infrastructure.repository.persistence.entity.MemberEntity;

public class MemberMapper {

    public static MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .id(member.getId())
                .username(member.getUsername())
                .password(member.getPassword())
                .createdAt(member.getCreatedAt())
                .updatedAt(member.getUpdatedAt())
                .build();
    }

    public static Member toModel(MemberEntity memberEntity) {
        return Member.builder()
                .id(memberEntity.getId())
                .username(memberEntity.getUsername())
                .password(memberEntity.getPassword())
                .createdAt(memberEntity.getCreatedAt())
                .updatedAt(memberEntity.getUpdatedAt())
                .build();
    }
}
