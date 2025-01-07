package com.boilerplate.back.dto.token;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RefreshTokenResponse {
    private final String refreshToken;
    private final int refreshTokenExpirationTime; // 단위: 초
}