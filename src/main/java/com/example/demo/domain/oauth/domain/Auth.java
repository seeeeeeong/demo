package com.example.demo.domain.oauth.domain;

import com.example.demo.common.entity.BaseEntity;
import com.example.demo.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Auth extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Column(nullable = false)
    private String refreshToken;
    private String sub;
    private String idToken;

    @Builder
    private Auth(User user, String refreshToken, String sub, String idToken) {
        this.user = user;
        this.refreshToken = refreshToken;
        this.sub = sub;
        this.idToken = idToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void update(String string, String refreshToken) {
        this.idToken = string;
        this.refreshToken =refreshToken;
    }
}
