package com.example.demo.common.security.jwt.exception;

import com.example.demo.common.exception.ErrorCode;

public class TokenNotFoundException extends TokenException {

    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotFoundException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }
}
