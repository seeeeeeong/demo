package com.example.demo.common.security.jwt.exception;

import com.example.demo.common.exception.ErrorCode;

public class ExpiredRefreshTokenException extends TokenException{

    public ExpiredRefreshTokenException() {
        super(ErrorCode.EXPIRED_JWT_REFRESH_TOKEN);
    }

}
