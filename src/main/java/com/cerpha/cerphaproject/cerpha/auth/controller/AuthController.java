package com.cerpha.cerphaproject.cerpha.auth.controller;

import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/email")
    public ResponseEntity<Object> sendEmail(@RequestParam("email") String email, HttpSession session) {
        int authNumber = authService.sendEmail(email);

        session.setAttribute(email, authNumber);

        return ResponseEntity.ok().body("이메일 인증");
    }

    @PostMapping("/users/email/verify")
    public ResponseEntity<Object> verifyEmail(@RequestBody EmailRequest emailRequest, HttpSession session) {
        boolean isVerified = authService.verifyEmail(emailRequest, session);

        return ResponseEntity.ok().body(isVerified);
    }

    @PostMapping("/users/signup")
    public ResponseEntity<Object> signupUser(@RequestBody SignUpUserRequest userRequest) {
        authService.signup(userRequest);

        return ResponseEntity.ok().body("회원가입 완료");
    }
}
