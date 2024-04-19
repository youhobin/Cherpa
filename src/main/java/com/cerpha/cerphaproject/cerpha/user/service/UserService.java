package com.cerpha.cerphaproject.cerpha.user.service;

import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.repository.UserRepository;
import com.cerpha.cerphaproject.cerpha.user.request.UpdatePasswordRequest;
import com.cerpha.cerphaproject.cerpha.user.request.UpdateProfileRequest;
import com.cerpha.cerphaproject.cerpha.user.response.UserProfileResponse;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        Users user = getUserById(userId);

        return new UserProfileResponse(user);
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

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
            throw new BusinessException(NOT_EQUAL_NEW_PASSWORD);
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        if (!isEqualPassword(request, user)) {
            throw new BusinessException(NOT_EQUAL_PREV_PASSWORD);
        }

        user.changePassword(request);
    }

    private boolean isEqualPassword(UpdatePasswordRequest request, Users user) {
        return passwordEncoder.matches(request.getPrevPassword(), user.getPassword());
    }

}
