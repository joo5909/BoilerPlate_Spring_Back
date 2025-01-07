package com.boilerplate.back.service.member;

import com.boilerplate.back.model.member.User;
import com.boilerplate.back.repository.member.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(int id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public User updateUser(int id, User newUserData) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setEmail(newUserData.getEmail());
        existingUser.setNickname(newUserData.getNickname());
        existingUser.setTelNumber(newUserData.getTelNumber());
        existingUser.setAddress(newUserData.getAddress());
        existingUser.setProfileImage(newUserData.getProfileImage());
        return userRepository.save(existingUser);
    }
}
