package com.cerpha.cerphaproject.cerpha.auth.service;

import com.cerpha.cerphaproject.cerpha.auth.repository.AuthRepository;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.user.domain.User;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthRepository authRepository;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    @Transactional
    public void signup(SignUpUserRequest userRequest) {
        // 암호화 추가
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .name(userRequest.getName())
                .nickname(userRequest.getNickname())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .role(UserRole.USER)
                .build();
        authRepository.save(user);
    }
}
