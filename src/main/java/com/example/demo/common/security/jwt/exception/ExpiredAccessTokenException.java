package com.example.demo.common.security.jwt.exception;

import com.example.demo.common.exception.ErrorCode;

public class ExpiredAccessTokenException extends TokenException{
    public ExpiredAccessTokenException() {
        super(ErrorCode.EXPIRED_JWT_ACCESS_TOKEN);
    }
}
