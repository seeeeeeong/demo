package com.example.demo.domain.user.service;

import com.example.demo.domain.user.domain.SocialCode;
import com.example.demo.domain.user.domain.User;
import com.example.demo.domain.user.exception.UserAccessDeniedException;
import com.example.demo.domain.user.exception.UserNotFoundException;
import com.example.demo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ValidateUserService {

    private final UserRepository userRepository;

    public User validateUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(UserNotFoundException::new);
    }

    public User validateRegisteredUserByEmail(String email, SocialCode socialCode) {
        return userRepository.findAllByEmail(email).stream()
                .filter(user -> user.getSocialCode() == socialCode)
                .findFirst()
                .orElse(null);
    }

    public User validateAdminUserById(Long userId) {
        User user = validateUserById(userId);
        if (user.isAdmin()) {
            return user;
        } else {
            throw new UserAccessDeniedException();
        }
    }
}
