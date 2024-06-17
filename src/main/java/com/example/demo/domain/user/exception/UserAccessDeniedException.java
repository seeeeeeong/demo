package com.example.demo.domain.user.exception;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;

public class UserAccessDeniedException extends BusinessException {

    public UserAccessDeniedException() {
        super(ErrorCode.USER_ACCESS_DENIED);
    }

}
