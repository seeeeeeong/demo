package com.example.demo.common.security.jwt;

import com.example.demo.common.exception.BusinessException;
import com.example.demo.common.exception.ErrorCode;
import com.example.demo.common.security.jwt.exception.ExpiredAccessTokenException;
import com.example.demo.common.security.jwt.exception.ExpiredRefreshTokenException;
import com.example.demo.common.security.jwt.exception.InvalidTokenException;
import com.example.demo.domain.user.domain.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.stream.Collectors;

@Slf4j
@Component
@PropertySource("classpath:application-oauth.yml")
public class JwtTokenProvider {

    private final String CLAIM_USER_ID = JwtProperties.USER_ID;
    private final String CLAIM_USER_ROLE = JwtProperties.ROLE;

    private final long ACCESS_TOKEN_EXPIRE_TIME;
    private final long REFRESH_TOKEN_EXPIRE_TIME;

    private final Key key;
    private final ObjectMapper objectMapper;

    public JwtTokenProvider(@Value("${jwt.access-token-expire-time}") long accessTime,
                            @Value("${jwt.refresh-token-expire-time}") long refreshTime,
                            @Value("${jwt.secret}") String secretKey) {
        this.ACCESS_TOKEN_EXPIRE_TIME = accessTime;
        this.REFRESH_TOKEN_EXPIRE_TIME = refreshTime;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.objectMapper = new ObjectMapper();
    }

    protected String createToken(Long userId, UserRole userRole, long tokenValid) {

        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");

        Claims claims = Jwts.claims();

        claims.put(CLAIM_USER_ID, userId.toString());
        claims.put(CLAIM_USER_ROLE, userRole);

        Date date = new Date();

        return Jwts.builder()
                .setHeader(header)
                .setClaims(claims) // 토큰 발행 유저 정보
                .setIssuedAt(date) // 토큰 발행 시간
                .setExpiration(new Date(date.getTime() + tokenValid)) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS512) // 알고리즘과 키 설정
                .compact();
    }

    public String createAccessToken(Long userId, UserRole userRole) {
        return createToken(userId, userRole, ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(Long userId, UserRole userRole) {
        return createToken(userId, userRole, REFRESH_TOKEN_EXPIRE_TIME);
    }

    public String expireToken(String jwtToken) {
        Claims claims = parseClaims(jwtToken);
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date())
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createNewAccessTokenFromRefreshToken(String refreshToken) {
//        Claims claims = parseClaims(refreshToken);
//
//        Long userId = Long.parseLong((String) claims.get(CLAIM_USER_ID));
//        UserRole role = UserRole.valueOf((String) claims.get(CLAIM_USER_ROLE));
//        return createAccessToken(userId, role);
        String payload = new String(Base64.getUrlDecoder().decode(refreshToken.split("\\.")[1]));
        try {
            JsonNode jsonNode = objectMapper.readTree(payload);
            Long userId = jsonNode.get(CLAIM_USER_ID).asLong();
            UserRole role = UserRole.valueOf(jsonNode.get(CLAIM_USER_ROLE).asText());
            return createAccessToken(userId, role);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 쿠키 maxAge는 초단위 설정이라 1000으로 나눈값으로 설정
     */
    public long getExpireTime() {
        return REFRESH_TOKEN_EXPIRE_TIME / 1000;
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(CLAIM_USER_ROLE) == null || !StringUtils.hasText(
                claims.get(CLAIM_USER_ROLE).toString())) {
            throw new BusinessException(ErrorCode.AUTHORITY_NOT_FOUND); //유저권한없음
        }

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(CLAIM_USER_ROLE).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new JwtAuthenticationToken(claims, "", authorities);
    }

    public void validAccessToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredAccessTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    public void validRefreshToken(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new ExpiredRefreshTokenException();
        } catch (Exception e) {
            throw new InvalidTokenException(e);
        }
    }

    public Claims parseClaims(String accessToken) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
    }

}

