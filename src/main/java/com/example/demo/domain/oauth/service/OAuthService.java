package com.example.demo.domain.oauth.service;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.domain.user.domain.SocialCode;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.service.ValidateUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.common.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OAuthService {

    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;
    private final ValidateUserService validateUserService;

    @Transactional
    public void deleteAccount(Long userId) {
        User user = validateUserService.validateUserById(userId);
        if (user.getSocialCode() == SocialCode.GOOGLE) {
            googleOAuthService.deleteAccount(user);
        } else if (user.getSocialCode() == SocialCode.APPLE) {
            appleOAuthService.deleteAccount(user);
        } else {
            throw new BusinessException(INTERNAL_SERVER_ERROR);
        }
    }
}
