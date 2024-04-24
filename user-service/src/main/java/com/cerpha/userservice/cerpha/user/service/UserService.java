package com.cerpha.userservice.cerpha.user.service;

import com.cerpha.userservice.cerpha.user.domain.Users;
import com.cerpha.userservice.cerpha.user.repository.UserRepository;
import com.cerpha.userservice.cerpha.user.request.UpdatePasswordRequest;
import com.cerpha.userservice.cerpha.user.request.UpdateProfileRequest;
import com.cerpha.userservice.cerpha.user.response.UserProfileResponse;
import com.cerpha.userservice.common.exception.BusinessException;
import com.cerpha.userservice.common.redis.RedisService;
import com.cerpha.userservice.common.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cerpha.userservice.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RedisService redisService, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public UserProfileResponse getUserProfile(Long userId) {
        Users user = getUserById(userId);

        return new UserProfileResponse(user);
    }

    @Transactional
    public void updateUserProfile(Long userId, UpdateProfileRequest request) {
        Users user = getUserById(userId);

        user.updateProfile(request.getAddress(), request.getPhone());
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordRequest request, String token) {
        if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
            throw new BusinessException(NOT_EQUAL_NEW_PASSWORD);
        }

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(NOT_FOUND_USER));

        if (!isEqualPassword(request, user)) {
            throw new BusinessException(NOT_EQUAL_PREV_PASSWORD);
        }

        user.changePassword(request.getNewPassword());

        String accessToken = token.substring(7);
        long time = jwtTokenProvider.parseClaims(accessToken).getExpiration().getTime() - System.currentTimeMillis();
        redisService.setBlackList(accessToken, String.valueOf(user.getId()), time);
        redisService.deleteRefreshToken(String.valueOf(user.getId()));
    }

    private boolean isEqualPassword(UpdatePasswordRequest request, Users user) {
        return passwordEncoder.matches(request.getPrevPassword(), user.getPassword());
    }

}
