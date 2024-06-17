package com.example.demo.domain.user.service;

import com.example.demo.domain.oauth.domain.Auth;
import com.example.demo.domain.oauth.dto.AppleIdToken;
import com.example.demo.domain.oauth.repository.AuthRepository;
import com.example.demo.domain.user.domain.SocialCode;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthRepository authRepository;

    @Transactional
    public User registerGoogleUser(String email, SocialCode socialCode, String refreshToken) {
        User user = userRepository.save(User.create(email, socialCode));
        Auth auth = Auth.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        authRepository.save(auth);
        return user;
    }

    @Transactional
    public User registerAppleUser(AppleIdToken appleIdToken, String refreshToken) {
        User user = userRepository.save(User.create(appleIdToken.getEmail(), SocialCode.APPLE));
        Auth auth = Auth.builder()
                .user(user)
                .idToken(appleIdToken.toString())
                .sub(appleIdToken.getSub())
                .refreshToken(refreshToken)
                .build();
        authRepository.save(auth);
         return user;
    }
}
