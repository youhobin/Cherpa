package com.cerpha.userservice.cerpha.auth.controller;

import com.cerpha.userservice.cerpha.auth.request.EmailRequest;
import com.cerpha.userservice.cerpha.auth.request.LogoutRequest;
import com.cerpha.userservice.cerpha.auth.request.ReissueTokenRequest;
import com.cerpha.userservice.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.userservice.cerpha.auth.response.TokenResponse;
import com.cerpha.userservice.cerpha.auth.response.VerifyEmailResponse;
import com.cerpha.userservice.cerpha.auth.service.AuthService;
import com.cerpha.userservice.common.dto.ResultDto;
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

    /**
     * 이메일 인증 코드 발송
     * @param email
     * @return
     */
    @PostMapping("/users/email")
    public ResponseEntity<ResultDto> sendEmail(@RequestParam("email") String email) {
        authService.sendEmail(email);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * 이메일 인증 코드 유효성 검사
     * @param emailRequest
     * @return
     */
    @PostMapping("/users/email/verify")
    public ResponseEntity<ResultDto<VerifyEmailResponse>> verifyEmail(@Valid @RequestBody EmailRequest emailRequest) {
        VerifyEmailResponse verifyEmailResponse = authService.verifyEmail(emailRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, verifyEmailResponse));
    }

    /**
     * 회원 가입
     * @param userRequest
     * @return
     */
    @PostMapping("/users/signup")
    public ResponseEntity<ResultDto> signupUser(@Valid @RequestBody SignUpUserRequest userRequest) {
        authService.signup(userRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }

    /**
     * 토큰 재발급
     * @param reissueTokenRequest
     * @return
     */
    @PostMapping("/users/reissue")
    public ResponseEntity<ResultDto<TokenResponse>> reissueToken(@Valid @RequestBody ReissueTokenRequest reissueTokenRequest) {
        TokenResponse tokenResponse = authService.reissueToken(reissueTokenRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK, tokenResponse));
    }

    /**
     * 로그아웃
     * @param logoutRequest
     * @return
     */
    @PostMapping("/users/logout")
    public ResponseEntity<ResultDto> logout(@Valid @RequestBody LogoutRequest logoutRequest) {
        authService.logout(logoutRequest);

        return ResponseEntity.ok(new ResultDto<>(HttpStatus.OK));
    }
}
