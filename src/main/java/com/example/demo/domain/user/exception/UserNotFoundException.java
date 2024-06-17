package com.example.demo.domain.user.exception;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }

}
