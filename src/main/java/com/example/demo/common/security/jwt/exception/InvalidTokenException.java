package com.example.demo.common.security.jwt.exception;

import com.example.demo.common.exception.ErrorCode;

public class InvalidTokenException extends TokenException{

    public InvalidTokenException(Throwable e) {
        super(ErrorCode.INVALID_TOKEN, e);
    }

    public InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
