package com.example.member.application;

import com.example.member.application.port.DateHolder;
import com.example.member.application.port.MemberRepository;
import com.example.member.common.dto.request.LoginRequest;
import com.example.member.common.dto.request.LogoutRequest;
import com.example.member.common.dto.request.MemberCreateRequest;
import com.example.member.common.dto.request.ReissueTokenRequest;
import com.example.member.common.dto.response.LoginResponse;
import com.example.member.common.dto.response.MemberCreateResponse;
import com.example.member.common.dto.response.ReissueTokenResponse;
import com.example.member.domain.exception.MemberServiceException;
import com.example.member.domain.model.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.member.domain.exception.ErrorCode.MEMBER_ALREADY_EXISTS;
import static com.example.member.domain.exception.ErrorCode.PASSWORD_UNMATCHED;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final DateHolder dateHolder;

    public MemberCreateResponse join(MemberCreateRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new MemberServiceException(MEMBER_ALREADY_EXISTS);
        }

        Member member = memberRepository.save(
                Member.create(
                        request.username(), passwordEncoder.encode(request.password())
                )
        );

        return MemberCreateResponse.from(member);
    }

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username());
        if (passwordUnMatched(request, member)) {
            throw new MemberServiceException(PASSWORD_UNMATCHED);
        }

        Date now = dateHolder.now();

        String accessToken = tokenService.generateAccessToken(member.getId(), now);
        String refreshToken = tokenService.generateRefreshToken(member.getId(), now);

        tokenService.saveRefreshToken(member.getId(), refreshToken, now);

        return new LoginResponse(accessToken, refreshToken);
    }

    public ReissueTokenResponse reissueToken(ReissueTokenRequest request) {

        return tokenService.reissueToken(request.refreshToken(), dateHolder.now());
    }

    public void logout(Long memberId, LogoutRequest request) {

        tokenService.logout(memberId, request.accessToken());
    }

    private boolean passwordUnMatched(LoginRequest request, Member member) {
        return !passwordEncoder.matches(request.password(), member.getPassword());
    }

}
