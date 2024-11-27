package com.example.member.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Member {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Member create(String username, String password) {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }
}
