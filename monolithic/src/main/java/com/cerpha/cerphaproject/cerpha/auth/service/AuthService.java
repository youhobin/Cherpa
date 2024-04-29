package com.cerpha.cerphaproject.cerpha.auth.service;

import com.cerpha.cerphaproject.cerpha.auth.repository.AuthRepository;
import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.LogoutRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.ReissueTokenRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.auth.response.TokenResponse;
import com.cerpha.cerphaproject.cerpha.auth.response.VerifyEmailResponse;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.redis.RedisService;
import com.cerpha.cerphaproject.common.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Random;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
@Service
public class AuthService implements UserDetailsService {

    @Value("${env.mail.subject}")
    private String mailSubject;

    @Value("${env.mail.mailer}")
    private String mailer;

    @Value("${env.mail.emailTemplate}")
    private String emailTemplate;

    private final AuthRepository authRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AuthRepository authRepository,
                       JavaMailSender mailSender,
                       BCryptPasswordEncoder passwordEncoder,
                       RedisService redisService, JwtTokenProvider jwtTokenProvider) {
        this.authRepository = authRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public void sendEmail(String email) {
        if (authRepository.findByEmail(email).isPresent()) {
            throw new BusinessException(DUPLICATED_EMAIL);
        }

        int randomNumber = createAuthNumber();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(mailer);
        message.setSubject(mailSubject);
        message.setText(emailTemplate.replace("{number}", String.valueOf(randomNumber)));

        mailSender.send(message);

        redisService.saveEmailAuthNumber(email, randomNumber);
    }

    @Transactional
    public VerifyEmailResponse verifyEmail(EmailRequest request) {
        String authNumber = redisService.getEmailInfo(request.getEmail());

        boolean isVerified = authNumber.equals(String.valueOf(request.getAuthNumber()));
        if (!isVerified) {
            throw new BusinessException(INVALID_AUTH_NUMBER);
        }

        redisService.saveIsEmailVerified(request.getEmail(), true);


        return new VerifyEmailResponse(true);
    }

    @Transactional
    public void signup(SignUpUserRequest userRequest) {
        if (authRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new BusinessException(DUPLICATED_EMAIL);
        }

        String isVerified = redisService.getEmailInfo(userRequest.getEmail());

        if (!Boolean.parseBoolean(isVerified)) {
            throw new BusinessException(NOT_AUTH_EMAIL);
        }

        Users user = Users.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .nickname(userRequest.getNickname())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .role(UserRole.USER)
                .build();

        authRepository.save(user);
    }

    @Transactional(readOnly = true)
    public TokenResponse reissueToken(ReissueTokenRequest reissueTokenRequest) {
        Long userId = reissueTokenRequest.getUserId();

        String refreshToken = redisService.getRefreshToken(String.valueOf(userId));
        if (refreshToken == null || !refreshToken.equals(reissueTokenRequest.getRefreshToken())) {
            throw new BusinessException(INVALID_REFRESH_TOKEN);
        }

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(userId));
        return new TokenResponse(userId, accessToken);
    }

    @Transactional
    public void logout(LogoutRequest logoutRequest) {
        Claims accessTokenClaims = jwtTokenProvider.parseClaims(logoutRequest.getAccessToken());
        String userId = accessTokenClaims.getSubject();
        long time = accessTokenClaims.getExpiration().getTime() - System.currentTimeMillis();
        redisService.setBlackList(logoutRequest.getAccessToken(), userId, time);
        redisService.deleteRefreshToken(userId);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                true, true, true, true,
                new ArrayList<>());
    }

    public Users getUserDetailsByEmail(String email) {
        return authRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public Users getUserById(Long id) {
        return authRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(NOT_FOUND_USER.getMessage()));
    }

    private int createAuthNumber() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
