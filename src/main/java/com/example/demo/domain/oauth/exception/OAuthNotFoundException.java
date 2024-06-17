package com.example.demo.domain.oauth.exception;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;

public class OAuthNotFoundException extends BusinessException {

    public OAuthNotFoundException() {
        super(ErrorCode.OAUTH_NOT_FOUND);
    }

}
