package com.boilerplate.back.service.member;


import com.boilerplate.back.dto.response.ResponseDto;
import com.boilerplate.back.dto.response.user.GetUserResponseDto;
import com.boilerplate.back.model.member.User;
import com.boilerplate.back.repository.member.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;

    User user = null;

    @Override
    public ResponseEntity<? super GetUserResponseDto> getUser(String email) {
        try {
            user = userRepository.findByEmail(email);
            if (user == null) {
                return GetUserResponseDto.notExistedUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetUserResponseDto.success(user);
    }
}
