package com.cerpha.cerphaproject.cerpha.auth.controller;

import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.ReissueTokenRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.response.TokenResponse;
import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResultDto> verifyEmail(@Valid @RequestBody EmailRequest emailRequest, HttpSession session) {
        boolean isVerified = authService.verifyEmail(emailRequest, session);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, isVerified));
    }

    @PostMapping("/users/signup")
    public ResponseEntity<ResultDto> signupUser(@Valid @RequestBody SignUpUserRequest userRequest) {
        authService.signup(userRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @PostMapping("/users/reissue")
    public ResponseEntity<ResultDto<TokenResponse>> reissueToken(@RequestBody ReissueTokenRequest reissueTokenRequest) {
        TokenResponse tokenResponse = authService.reissueToken(reissueTokenRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, tokenResponse));
    }
}
