package com.example.member.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    MEMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 회원입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원입니다."),
    PASSWORD_UNMATCHED(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
    TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
    TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 토큰입니다."),
    TOKEN_UNMATCHED(HttpStatus.BAD_REQUEST, "토큰이 일치하지 않습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
