package com.cerpha.cerphaproject.cerpha.auth.controller;

import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.LogoutRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.ReissueTokenRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.response.TokenResponse;
import com.cerpha.cerphaproject.cerpha.auth.response.VerifyEmailResponse;
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
    public ResponseEntity<ResultDto> sendEmail(@RequestParam("email") String email) {
        authService.sendEmail(email);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    @PostMapping("/users/email/verify")
    public ResponseEntity<ResultDto<VerifyEmailResponse>> verifyEmail(@Valid @RequestBody EmailRequest emailRequest, HttpSession session) {
        VerifyEmailResponse verifyEmailResponse = authService.verifyEmail(emailRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, verifyEmailResponse));
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

    @PostMapping("/users/logout")
    public ResponseEntity<ResultDto> logout(@RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
