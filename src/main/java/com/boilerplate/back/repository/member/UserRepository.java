package com.boilerplate.back.repository.member;

import com.boilerplate.back.model.member.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}