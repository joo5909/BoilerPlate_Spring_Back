package com.boilerplate.back.service.auth;

import com.boilerplate.back.dto.request.auth.SignInRequestDto;
import com.boilerplate.back.dto.request.auth.SignUpRequestDto;
import com.boilerplate.back.dto.response.ResponseDto;
import com.boilerplate.back.dto.response.auth.SignInResponseDto;
import com.boilerplate.back.dto.response.auth.SignUpResponseDto;
import com.boilerplate.back.dto.token.AccessTokenResponse;
import com.boilerplate.back.dto.token.RefreshTokenResponse;
import com.boilerplate.back.model.member.User;
import com.boilerplate.back.provider.JwtProvider;
import com.boilerplate.back.repository.member.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService {

    @Value("${spring.expirationTime.refresh}")
    private int refreshTokenExpirationTime;

    private final UserRepository userRepository; // JPA Repository
    private final JwtProvider jwtProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto, HttpServletResponse response) {

        String accessToken = null;
        String refreshToken = null;

        User user;
        try {
            // 사용자가 입력한 정보
            String email = dto.getEmail();
            String password = dto.getPassword();

            // 이메일로 사용자 조회
            user = userRepository.findByEmail(email);

            // 이메일 정보 없을 시 Fail
            if (user == null) {
                return SignInResponseDto.signFail();
            }

            // DB에 저장된 암호화된 비밀번호
            String encodedPassword = user.getPassword();

            boolean isPasswordMatch = passwordEncoder.matches(password, encodedPassword);

            // 비밀번호 비교
            if (!isPasswordMatch) {
                return SignInResponseDto.signFail();
            }

            // AccessToken 및 RefreshToken 생성
            AccessTokenResponse newAccessTokenResponse = jwtProvider.createAccessToken(email);
            RefreshTokenResponse newRefreshTokenResponse = jwtProvider.createRefreshToken();

            accessToken = newAccessTokenResponse.getAccessToken();
            refreshToken = newRefreshTokenResponse.getRefreshToken();

            // RefreshToken Redis에 저장
            jwtProvider.insertRefreshTokenInRedis(email, refreshToken);

            // Refresh Token을 HttpOnly 쿠키로 설정
            Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setSecure(true); // HTTPS를 통해서만 쿠키를 전송
            refreshTokenCookie.setPath("/"); // 쿠키의 유효 경로 설정
            refreshTokenCookie.setMaxAge(refreshTokenExpirationTime); // 쿠키의 만료 시간 설정
            // 응답에 쿠키 추가
            response.addCookie(refreshTokenCookie);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        // 액세스 토큰만 응답 본문에 포함
        return SignInResponseDto.success(accessToken, user);
    }


    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String email = dto.getEmail();
            boolean existedEmail = userRepository.existsByEmail(email);
            if (existedEmail) return SignUpResponseDto.duplicateEmail();

            String nickName = dto.getNickname();
            boolean existedNickName = userRepository.existsByNickname(nickName);
            if (existedNickName) return SignUpResponseDto.duplicateNickname();

            String telNumber = dto.getTelNumber();
            boolean existedTelNumber = userRepository.existsByTelNumber(telNumber);
            if (existedTelNumber) return SignUpResponseDto.duplicateTelNumber();

            String address = dto.getAddress();
            String addressDetail = dto.getAddressDetail();

            boolean agreedPersonal = dto.getAgreedPersonal();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);

            User newUser = User.builder()
                    .email(email)
                    .password(encodedPassword)
                    .nickname(nickName)
                    .telNumber(telNumber)
                    .address(address)
                    .addressDetail(addressDetail)
                    .agreed_personal(agreedPersonal ? "Y" : "N")
                    .build();

            userRepository.save(newUser);

        } catch(Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return SignUpResponseDto.success();
    }

    public void logout(String refreshToken) {
        jwtProvider.deleteRefreshTokenInRedis(refreshToken);
    }

}
