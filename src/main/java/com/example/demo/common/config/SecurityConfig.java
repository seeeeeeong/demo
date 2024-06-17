package com.example.demo.common.config;

import com.example.demo.common.log.MDCRequestLoggingFilter;
import com.example.demo.common.security.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.common.security.jwt.JwtAuthenticationFilter;
import com.example.demo.common.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    private final String[] permitAllEndpointList = {
            "/oauth2/login",
            "/oauth2/google/login",
            "/oauth2/apple/login",
            "/oauth2/kakao/login",
            "/oauth2/refresh",
            "/oauth2/expiredJwt"
    };

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, permitAllEndpointList),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtAuthenticationEntryPoint(objectMapper()),
                        JwtAuthenticationFilter.class)
                .addFilterBefore(new MDCRequestLoggingFilter(), JwtAuthenticationEntryPoint.class);

        return http.build();
    }

}
