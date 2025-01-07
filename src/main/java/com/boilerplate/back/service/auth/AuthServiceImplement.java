package com.boilerplate.back.service.auth;


import com.boilerplate.back.dto.response.ResponseDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import com.boilerplate.back.dto.token.AccessTokenResponse;
import com.boilerplate.back.dto.token.RefreshTokenResponse;
import com.boilerplate.back.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    private boolean loginCheck(String userId, String userPwd) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", userId);
        //Classic asp에서 암호화할때 대소문자.. 못가림
        map.put("pwd", userPwd.toLowerCase());

        List<MemberDto> mDto = memberDao.loginCheck(map);

        return !mDto.isEmpty();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

        String accessToken = null;
        String refreshToken = null;

        try {
            String userId = dto.getId();
            String userPwd = dto.getPassword();

            boolean loginResult = loginCheck(userId, userPwd);

            if (!loginResult) {
                return SignInResponseDto.signFail();
            }

            AccessTokenResponse newAccessTokenResponse = jwtProvider.createAccessToken(userId);
            RefreshTokenResponse newRefreshTokenResponse = jwtProvider.createRefreshToken();

            accessToken = newAccessTokenResponse.getAccessToken();
            refreshToken = newRefreshTokenResponse.getRefreshToken();


            jwtProvider.insertRefreshTokenInRedis(userId, refreshToken);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return SignInResponseDto.success(accessToken, refreshToken);
    }

    public void logout(String refreshToken) {
        jwtProvider.deleteRefreshTokenInRedis(refreshToken);
    }

}
