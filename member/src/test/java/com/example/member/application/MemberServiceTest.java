package com.example.member.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    MemberService memberService;

    @Test
    void 존재하지_않는_회원을_조회하면_예외가_발생한다() {
        //given
        given(memberRepository.findById(1L))
                .willReturn(Optional.empty());

        //then
        assertThrows(IllegalArgumentException.class,
                // when
                () -> memberService.findById(1L));
    }

}