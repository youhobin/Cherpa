package com.cerpha.userservice.cerpha.user.controller;

import com.cerpha.userservice.cerpha.user.service.UserService;
import com.cerpha.userservice.common.dto.ResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/users")
public class UserInternalController {

    private final UserService userService;

    public UserInternalController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResultDto<Long>> getUserId(@PathVariable("userId") Long userId) {
        Long savedUserId = userService.getUserId(userId);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, savedUserId));
    }
}
