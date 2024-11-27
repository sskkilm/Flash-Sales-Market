package com.example.member.presentation;

import com.example.member.application.MemberService;
import com.example.member.common.dto.request.LoginRequest;
import com.example.member.common.dto.request.LogoutRequest;
import com.example.member.common.dto.request.MemberCreateRequest;
import com.example.member.common.dto.request.ReissueTokenRequest;
import com.example.member.common.dto.response.LoginResponse;
import com.example.member.common.dto.response.MemberCreateResponse;
import com.example.member.common.dto.response.ReissueTokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private static final String X_MEMBER_ID = "X-Member-Id";
    private final MemberService memberService;

    @PostMapping("/join")
    public MemberCreateResponse join(@RequestBody @Valid MemberCreateRequest request) {
        return memberService.join(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody @Valid LoginRequest request) {
        return memberService.login(request);
    }

    @PostMapping("/reissue")
    public ReissueTokenResponse reissueToken(@RequestBody @Valid ReissueTokenRequest request) {
        return memberService.reissueToken(request);
    }

    @PostMapping("/logout")
    public void logout(
            @RequestHeader(X_MEMBER_ID) Long memberId,
            @RequestBody @Valid LogoutRequest request
    ) {
        memberService.logout(memberId, request);
    }
}
