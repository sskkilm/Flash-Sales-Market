package com.example.member.infrastructure;

import com.example.member.domain.Member;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "Member")
@EntityListeners(AuditingEntityListener.class)
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    private String name;

    private String phoneNumber;

    private String address;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Member toModel() {
        return Member.builder()
                .id(this.id)
                .email(this.email)
                .name(this.name)
                .phoneNumber(this.phoneNumber)
                .address(this.address)
                .createdAt(this.createdAt)
                .updatedAt(this.updatedAt)
                .build();
    }
}
