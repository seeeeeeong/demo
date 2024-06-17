package com.example.demo.domain.user.exception;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;

public class DuplicateUserException extends BusinessException {

    public DuplicateUserException() {
        super(ErrorCode.DUPLICATE_USER);
    }
}
