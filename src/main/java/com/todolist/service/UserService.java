package com.todolist.service;

import com.todolist.domain.User;
import com.todolist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    // Constructor Injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 사용자 생성
    public User saveUser(User user) {
        // 중복 아이디 체크
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }
        return userRepository.save(user);
    }


    // 사용자 조회 (ID 기준)
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
