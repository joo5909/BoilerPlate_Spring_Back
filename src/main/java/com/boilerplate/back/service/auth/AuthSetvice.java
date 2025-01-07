package com.boilerplate.back.service.auth;


import com.boilerplate.back.dto.request.auth.SignInRequestDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);

    void logout(String refreshToken);
}
