package com.boilerplate.back.service.auth;


import com.boilerplate.back.dto.request.auth.SignInRequestDto;
import com.boilerplate.back.dto.request.auth.SignUpRequestDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import com.boilerplate.back.dto.response.auth.SignUpResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response);

    ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);

    void logout(String refreshToken);
}
