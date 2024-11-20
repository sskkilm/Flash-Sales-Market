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
        for (int i = 0; i < 1000; i++) {
            Member member = Member.builder()
                    .email("email@" + i)
                    .name("name@" + i)
                    .build();
            memberRepository.save(member);
        }
    }
}
