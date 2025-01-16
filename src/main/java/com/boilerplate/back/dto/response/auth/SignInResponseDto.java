package com.boilerplate.back.dto.response.auth;

import com.boilerplate.back.common.ResponseCode;
import com.boilerplate.back.common.ResponseMessage;
import com.boilerplate.back.dto.response.ResponseDto;
import com.boilerplate.back.model.member.User;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@JsonPropertyOrder({"code", "message", "accessToken", "email", "nickname", "profileImage"}) // 원하는 순서로 키 설정
public class SignInResponseDto extends ResponseDto {

    private final String accessToken;
    private final String email;
    private final String nickname;
    private final String profileImage;

    // 리프레시 토큰을 포함하지 않음
    private SignInResponseDto(String accessToken, String email, String nickname, String profileImage) {
        super();
        this.accessToken = accessToken;
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    // 성공 응답 생성
    public static ResponseEntity<SignInResponseDto> success(String accessToken, User user) {
        SignInResponseDto responseBody = new SignInResponseDto(
                accessToken,
                user.getEmail(),
                user.getNickname(),
                user.getProfileImage()

        );
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    // 로그인 실패 응답
    public static ResponseEntity<ResponseDto> signFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
