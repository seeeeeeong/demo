package com.example.demo.domain.oauth.repository;

import com.example.demo.domain.oauth.domain.Auth;
import com.example.demo.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    Optional<Auth> findByUser(User user);
}
