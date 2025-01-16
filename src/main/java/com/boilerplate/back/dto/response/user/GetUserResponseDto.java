package com.boilerplate.back.dto.response.user;

import com.boilerplate.back.common.ResponseCode;
import com.boilerplate.back.common.ResponseMessage;
import com.boilerplate.back.dto.response.ResponseDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import com.boilerplate.back.model.member.User;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class GetUserResponseDto extends ResponseDto {
    private final String email;
    private final String nickname;
    private final String telNumber;
    private final String address;
    private final String addressDetail;
    private final String ProfileImage;

    private GetUserResponseDto(User user) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.telNumber = user.getTelNumber();
        this.address = user.getAddress();
        this.addressDetail = user.getAddressDetail();
        this.ProfileImage = user.getProfileImage();

    }

    public static ResponseEntity<GetUserResponseDto> success(User user) {
        GetUserResponseDto responseBody = new GetUserResponseDto(user);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistedUser() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
