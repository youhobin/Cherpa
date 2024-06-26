package com.cerpha.userservice.cerpha.user.controller;

import com.cerpha.userservice.cerpha.user.request.UpdatePasswordRequest;
import com.cerpha.userservice.cerpha.user.request.UpdateProfileRequest;
import com.cerpha.userservice.cerpha.user.response.UserProfileResponse;
import com.cerpha.userservice.cerpha.user.service.UserService;
import com.cerpha.userservice.common.dto.ResultDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 마이페이지 조회
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ResultDto<UserProfileResponse>> getUserProfile(@PathVariable("userId") Long userId) {
        UserProfileResponse userProfileResponse = userService.getUserProfile(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, userProfileResponse));
    }

    /**
     * 마이페이지 수정
     * @param userId
     * @param updateProfileRequest
     * @return
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<ResultDto> updateUserProfile(@PathVariable("userId") Long userId,
                                                       @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        userService.updateUserProfile(userId, updateProfileRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * 비밀번호 변경
     * @param userId
     * @param updatePasswordRequest
     * @param token
     * @return
     */
    @PutMapping("/users/{userId}/password")
    public ResponseEntity<ResultDto> updateUserPassword(@PathVariable("userId") Long userId,
                                                        @Valid @RequestBody UpdatePasswordRequest updatePasswordRequest,
                                                        @RequestHeader(value = "Authorization") String token) {
        userService.updatePassword(userId, updatePasswordRequest, token);
        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

}
