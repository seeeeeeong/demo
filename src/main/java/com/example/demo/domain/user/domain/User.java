package com.example.demo.domain.user.domain;

import com.example.demo.common.entity.BaseEntityWithUpdate;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "\"user\"")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "deleted_at is null")
public class User extends BaseEntityWithUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Column(nullable = false)
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialCode socialCode;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private LocalDateTime deletedAt;

    @Builder(access = AccessLevel.PRIVATE)
    private User(String email, SocialCode socialCode) {
        this.email = email;
        this.socialCode = socialCode;
        this.userRole = UserRole.USER;
    }

    public static User create(String email, SocialCode socialCode) {
        return User.builder()
                .email(email)
                .socialCode(socialCode)
                .build();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void deleteUser() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isAdmin() {
        return this.userRole == UserRole.ADMIN;
    }
}
