package com.example.member;

import com.example.member.application.MemberRepository;
import com.example.member.domain.Member;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
@RequiredArgsConstructor
public class TestMemberDataInit {

    private final MemberRepository memberRepository;

    @PostConstruct
    public void init() {
        Member member = Member.builder()
                .email("email")
                .name("name")
                .build();
        memberRepository.save(member);
    }
}
