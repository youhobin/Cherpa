package com.cerpha.cerphaproject.cerpha.auth.controller;

import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/email")
    public ResponseEntity<ResultDto> sendEmail(@RequestParam("email") String email, HttpSession session) {
        int authNumber = authService.sendEmail(email);

        session.setAttribute(email, authNumber);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @PostMapping("/users/email/verify")
    public ResponseEntity<ResultDto> verifyEmail(@RequestBody EmailRequest emailRequest, HttpSession session) {
        boolean isVerified = authService.verifyEmail(emailRequest, session);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, isVerified));
    }

    @PostMapping("/users/signup")
    public ResponseEntity<ResultDto> signupUser(@RequestBody SignUpUserRequest userRequest) {
        authService.signup(userRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
