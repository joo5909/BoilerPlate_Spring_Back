package com.boilerplate.back.filter;

import com.boilerplate.back.dto.token.AccessTokenResponse;
import com.boilerplate.back.dto.token.RefreshTokenResponse;
import com.boilerplate.back.provider.JwtProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Value("${spring.expirationTime.refresh}")
    private int refreshTokenExpirationTime;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = parseBearerToken(request);

            if (accessToken == null){
                filterChain.doFilter(request, response);
                return;
            }

            String userId;

            try {
                userId = jwtProvider.validateAccessToken(accessToken); // AccessToken 검증
            } catch (ExpiredJwtException e) {

                // AccessToken 만료 시 쿠키에 있는 RefreshToken 가져옴
                String refreshToken = parseRefreshToken(request);
                if (refreshToken != null && jwtProvider.validateRefreshToken(refreshToken)) {

                    //redis에 있는지 확인
                    userId = jwtProvider.getSubjectFromRedis(refreshToken); // userId 추출

                    AccessTokenResponse newAccessTokenResponse = jwtProvider.createAccessToken(userId);
                    RefreshTokenResponse newRefreshTokenResponse = jwtProvider.createRefreshToken();

                    // 새 AccessToken 및 RefreshToken 생성
                    String newAccessToken = newAccessTokenResponse.getAccessToken();

                    String newRefreshToken = newRefreshTokenResponse.getRefreshToken();

                    // 새 RefreshToken 저장소 업데이트
                    jwtProvider.updateRefreshTokenInRedis(userId, refreshToken, newRefreshToken);

                    // 새 RefreshToken을 HttpOnly 쿠키로 설정
                    Cookie newRefreshTokenCookie = new Cookie("refreshToken", newRefreshToken);
                    newRefreshTokenCookie.setHttpOnly(true);  // JavaScript에서 접근 불가능
                    newRefreshTokenCookie.setSecure(true);    // HTTPS로만 전송
                    newRefreshTokenCookie.setPath("/");       // 쿠키의 유효 경로 설정
                    newRefreshTokenCookie.setMaxAge(refreshTokenExpirationTime); // 만료 시간 설정

                    // 응답에 쿠키 추가
                    response.addCookie(newRefreshTokenCookie);

                } else {
                    // 쿠키에 있는 RefreshToken 값이 없거나 유효하지 않을시 401 에러
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 인증 실패
                    response.getWriter().write("Authentication failed, please log in again.");
                    return; // 더 이상 진행하지 않음
                }
            }

            // 유효한 AccessToken에 대해 SecurityContext 설정
            if (userId != null) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                AbstractAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userId, null, authorities);

                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authenticationToken);
                SecurityContextHolder.setContext(securityContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }

    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return (authorization != null && authorization.startsWith("Bearer "))
                ? authorization.substring(7)
                : null;
    }

    private String parseRefreshToken(HttpServletRequest request) {
        // 쿠키에서 RefreshToken을 찾는 방법
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
