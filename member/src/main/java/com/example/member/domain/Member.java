package com.example.member.domain;

import java.time.LocalDateTime;

public class Member {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private Address address;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
