package com.example.demo.domain.user.repository;

import com.example.demo.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Long userId);
    List<User> findAllByEmail(String email);

}
