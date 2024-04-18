package com.cerpha.cerphaproject.cerpha.user.controller;

import com.cerpha.cerphaproject.cerpha.user.response.UserResponse;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import com.cerpha.cerphaproject.cerpha.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResultDto<UserResponse>> getUserProfile(@PathVariable("userId") Long userId) {
        UserResponse userResponse = userService.getUserProfile(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, userResponse));
    }
}
