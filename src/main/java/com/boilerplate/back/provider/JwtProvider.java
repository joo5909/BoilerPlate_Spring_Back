package com.boilerplate.back.provider;

import com.boilerplate.back.dto.token.AccessTokenResponse;
import com.boilerplate.back.dto.token.RefreshTokenResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${spring.expirationTime.access}")
    private int accessTokenExpirationTime;

    @Value("${spring.expirationTime.refresh}")
    private int refreshTokenExpirationTime;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public AccessTokenResponse createAccessToken(String userId) {
        Date expiredDate = Date.from(Instant.now().plus(accessTokenExpirationTime, ChronoUnit.SECONDS));
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        String accessToken = Jwts.builder()
                .signWith(key)
                .subject(userId)
                .expiration(expiredDate)
                .compact();
        return new AccessTokenResponse(accessToken, accessTokenExpirationTime);

    }

    // RefreshToken 발급
    public RefreshTokenResponse createRefreshToken() {
        Date expiredDate = Date.from(Instant.now().plus(refreshTokenExpirationTime, ChronoUnit.SECONDS)); // 7일 유효
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        String refreshToken = Jwts.builder()
                .signWith(key)
                .expiration(expiredDate)
                .compact();

        return new RefreshTokenResponse(refreshToken, refreshTokenExpirationTime);
    }

    // AccessToken 검증 및 Subject 반환
    public String validateAccessToken(String jwt) throws ExpiredJwtException {
        return validateToken(jwt);
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String jwt) {
        try {
            return validateToken(jwt) != null; // 유효한 토큰일 경우 true 반환
        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우 false 반환
            return false;
        }
    }

    // RefreshToken 저장소에서 userId 가져오기
    public String getSubjectFromRedis(String refreshToken) {
        // Redis에서 refreshToken을 키로 사용하여 userId를 조회
        String userId = redisTemplate.opsForValue().get(refreshToken);
        return userId != null ? userId : null;
    }

    // RefreshToken 저장소 업데이트
    public void updateRefreshTokenInRedis(String userId, String oldRefreshToken, String newRefreshToken) {

        String existingRefreshValue = redisTemplate.opsForValue().get(oldRefreshToken);

        if (existingRefreshValue != null) {
            // 기존 RefreshToken이 있으면 삭제
            redisTemplate.delete(oldRefreshToken);
            System.out.println("Existing RefreshToken deleted for userId: " + userId);
        }

        redisTemplate.opsForValue().set(newRefreshToken, userId, refreshTokenExpirationTime, TimeUnit.SECONDS); // 7일간 유효
    }

    // RefreshToken 저장소 입력
    public void insertRefreshTokenInRedis(String userId, String newRefreshToken) {
        //기존 토큰 살아있어도, 새로 발급. (기존토큰은 일주일뒤 자동삭제.. 로그인시에는 기존 토큰값 알수가 없음)
        redisTemplate.opsForValue().set(newRefreshToken, userId, refreshTokenExpirationTime, TimeUnit.SECONDS); // 7일간 유효
    }


    public void deleteRefreshTokenInRedis(String refreshToken) {
        redisTemplate.delete(refreshToken);
    }

    public String validateToken(String jwt) throws ExpiredJwtException {

        String subject = null;
        SecretKey key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt);  // 여기서 Jws<Claims> 반환

            subject = claimsJws.getPayload().getSubject();  // subject 추출

        } catch (ExpiredJwtException e) {
            // 만료된 토큰의 경우 ExpiredJwtException을 명시적으로 던지기
            throw e;
        } catch (JwtException | IllegalArgumentException e) {
            // 유효하지 않거나 잘못된 토큰에 대해서는 null 반환
            e.printStackTrace();
            return null;
        }
        subject = subject != null ? subject : "";
        return subject;
    }
}
