package com.boilerplate.back.dto.response.auth;



import com.boilerplate.back.common.ResponseCode;
import com.boilerplate.back.common.ResponseMessage;
import com.boilerplate.back.dto.response.ResponseDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SignInResponseDto extends ResponseDto {

    private final String accessToken;
    private final String refreshToken;

    private SignInResponseDto(String accessToken, String refreshToken) {
        super();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public static ResponseEntity<SignInResponseDto> success(String accessToken, String refreshToken){
        SignInResponseDto responseBody = new SignInResponseDto(accessToken, refreshToken);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signFail() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL, ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
