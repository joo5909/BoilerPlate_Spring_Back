package com.boilerplate.back.dto.request.auth;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignUpRequestDto {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8, max = 20, message = "비밀번호는 8자에서 20자 사이여야 합니다.")
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank @Pattern(regexp = "^01[016789]-\\d{3,4}-\\d{4}$")
    private String telNumber;

    @NotBlank
    private String address;

    private String addressDetail;

    @NotNull @AssertTrue
    private Boolean agreedPersonal;
}
