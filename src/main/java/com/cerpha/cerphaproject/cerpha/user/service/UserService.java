package com.cerpha.cerphaproject.cerpha.user.service;

import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.cerpha.user.request.UpdateProfileRequest;
import com.cerpha.cerphaproject.cerpha.user.response.UserResponse;
import com.cerpha.cerphaproject.common.encryption.AESEncryption;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getUserProfile(Long userId) {
        Users user = getUserById(userId);

        return new UserResponse(user);
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateProfileRequest request) {
        Users user = getUserById(userId);

        user.updateProfile(request);
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
    }
}
