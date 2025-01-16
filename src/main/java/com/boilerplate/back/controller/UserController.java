package com.boilerplate.back.controller;

import com.boilerplate.back.dto.response.user.GetUserResponseDto;
import com.boilerplate.back.service.member.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<? super GetUserResponseDto> getUser(@AuthenticationPrincipal String email) {
        ResponseEntity<? super GetUserResponseDto> response = userService.getUser(email);
        return response;
    }
}
