package com.cerpha.cerphaproject.cerpha.auth.service;

import com.cerpha.cerphaproject.cerpha.auth.repository.AuthRepository;
import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import com.cerpha.cerphaproject.common.encryption.AESEncryption;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionCode;
import jakarta.servlet.http.HttpSession;
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
    private final AESEncryption aesEncryption;

    public AuthService(AuthRepository authRepository,
                       JavaMailSender mailSender,
                       BCryptPasswordEncoder passwordEncoder, AESEncryption aesEncryption) {
        this.authRepository = authRepository;
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.aesEncryption = aesEncryption;
    }

    @Transactional
    public void signup(SignUpUserRequest userRequest) {
        if (authRepository.findByEmail(userRequest.getEmail()).isPresent()) {
            throw new BusinessException(DUPLICATED_EMAIL);
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

    public int sendEmail(String email) {
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

        return randomNumber;
    }

    public boolean verifyEmail(EmailRequest emailRequest, HttpSession session) {
        Integer savedAuthNum = (Integer) session.getAttribute(emailRequest.getEmail());

        if (savedAuthNum != null && emailRequest.getAuthNumber() == savedAuthNum) {
            return true;
        }
        return false;
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

    private int createAuthNumber() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
