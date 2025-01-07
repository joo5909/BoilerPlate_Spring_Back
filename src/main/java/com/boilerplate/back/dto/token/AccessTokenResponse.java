package com.boilerplate.back.dto.token;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenResponse {
    private final String accessToken;
    private final int accessTokenExpirationTime;
}