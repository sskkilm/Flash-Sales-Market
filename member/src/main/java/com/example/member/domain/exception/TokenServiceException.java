package com.example.member.domain.exception;

import lombok.Getter;

@Getter
public class TokenServiceException extends RuntimeException {

    private final ErrorCode errorCode;

    public TokenServiceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public TokenServiceException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }
}
