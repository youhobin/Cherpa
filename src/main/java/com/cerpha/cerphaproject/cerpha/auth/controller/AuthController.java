package com.cerpha.cerphaproject.cerpha.auth.controller;

import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/signup")
    public ResponseEntity<Object> signupUser(@RequestBody SignUpUserRequest userRequest) {
        authService.signup(userRequest);

        return ResponseEntity.ok().body("회원가입 완료");
    }
}
