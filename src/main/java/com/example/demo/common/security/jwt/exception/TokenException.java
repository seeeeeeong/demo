package com.example.demo.common.security.jwt.exception;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;

public class TokenException extends BusinessException {

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenException(ErrorCode errorCode, Throwable throwable) {
        super(errorCode, throwable);
    }}
