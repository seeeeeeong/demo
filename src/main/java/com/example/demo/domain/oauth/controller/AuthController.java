package com.example.demo.domain.oauth.controller;

import com.example.demo.common.resolver.AuthUser;
import com.example.demo.common.response.SuccessResponse;
import com.example.demo.common.security.jwt.JwtTokenInfo;
import com.example.demo.common.security.jwt.JwtTokenProvider;
import com.example.demo.common.security.jwt.JwtType;
import com.example.demo.common.utils.HeaderUtils;
import com.example.demo.domain.oauth.dto.RequestAppleLogin;
import com.example.demo.domain.oauth.dto.ResponseAccessToken;
import com.example.demo.domain.oauth.dto.ResponseJwtToken;
import com.example.demo.domain.oauth.service.AppleOAuthService;
import com.example.demo.domain.oauth.service.GoogleOAuthService;
import com.example.demo.domain.oauth.service.OAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class AuthController {

    private final OAuthService oAuthService;
    private final GoogleOAuthService googleOAuthService;
    private final AppleOAuthService appleOAuthService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/google/login")
    public ResponseEntity<SuccessResponse<ResponseJwtToken>> googleLogin(HttpServletRequest request) {
        return SuccessResponse.of(googleOAuthService.login(request)).asHttp(HttpStatus.OK);
    }

    @PostMapping("/apple/login")
    public ResponseEntity<SuccessResponse<ResponseJwtToken>> appleLogin(HttpServletRequest request,
                                                                        @RequestBody @Valid RequestAppleLogin requestAppleLogin) {
        return SuccessResponse.of(appleOAuthService.login(request, requestAppleLogin)).asHttp(HttpStatus.OK);
    }

    @GetMapping("/refresh")
    public ResponseEntity<SuccessResponse<ResponseAccessToken>> getAccessToken(HttpServletRequest request) {
        String refreshToken = HeaderUtils.getJwtToken(request, JwtType.REFRESH);
        String accessToken = jwtTokenProvider.createNewAccessTokenFromRefreshToken(refreshToken);
        return SuccessResponse.of(ResponseAccessToken.of(accessToken)).asHttp(HttpStatus.OK);
    }

    @DeleteMapping("/users")
    public ResponseEntity<Void> deleteAccount(@AuthUser JwtTokenInfo tokenInfo) {
        oAuthService.deleteAccount(tokenInfo.getUserId());
        return ResponseEntity.noContent().build();
    }

}
