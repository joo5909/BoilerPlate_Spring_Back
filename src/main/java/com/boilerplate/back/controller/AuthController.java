package com.boilerplate.back.controller;

import com.boilerplate.back.dto.request.auth.SignInRequestDto;
import com.boilerplate.back.dto.request.auth.SignUpRequestDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import com.boilerplate.back.dto.response.auth.SignUpResponseDto;
import com.boilerplate.back.service.auth.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
class AuthController {
    private final AuthService authService;

    @Value("${spring.expirationTime.refresh}")
    private int refreshTokenExpirationTime;

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestBody, HttpServletResponse response) {
        // 서비스에서 액세스 토큰과 리프레시 토큰을 반환받고, 리프레시 토큰은 쿠키에 설정
        ResponseEntity<? super SignInResponseDto> signInResponse  = authService.signIn(requestBody, response);

        return signInResponse;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {

        return authService.signUp(requestBody);
    }

    @PostMapping("/log-out")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // 클라이언트에서 받은 refreshToken 쿠키 삭제
        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(true); // HTTPS를 통해서만 쿠키를 전송
        refreshTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
        refreshTokenCookie.setMaxAge(0); // 쿠키의 만료 시간 설정
        response.addCookie(refreshTokenCookie);

        // 서버 측에서 세션을 삭제 (예: Redis에서 세션 삭제)
        String refreshToken = getRefreshTokenFromRequest(request); // 요청에서 refreshToken을 추출
        authService.logout(refreshToken); // 서버 측에서 토큰을 삭제하는 서비스 호출

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request) {
        // 요청에서 쿠키를 추출하여 refreshToken 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
