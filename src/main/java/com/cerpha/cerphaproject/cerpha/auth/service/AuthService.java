package com.cerpha.cerphaproject.cerpha.auth.service;

import com.cerpha.cerphaproject.cerpha.auth.repository.AuthRepository;
import com.cerpha.cerphaproject.cerpha.auth.request.EmailRequest;
import com.cerpha.cerphaproject.cerpha.auth.request.SignUpUserRequest;
import com.cerpha.cerphaproject.cerpha.user.domain.User;
import com.cerpha.cerphaproject.cerpha.user.domain.UserRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
public class AuthService {

    @Value("${env.mail.subject}")
    private String mailSubject;

    @Value("${env.mail.mailer}")
    private String mailer;

    @Value("${env.mail.emailTemplate}")
    private String emailTemplate;

    private final AuthRepository authRepository;
    private final JavaMailSender mailSender;

    public AuthService(AuthRepository authRepository, JavaMailSender mailSender) {
        this.authRepository = authRepository;
        this.mailSender = mailSender;
    }

    @Transactional
    public void signup(SignUpUserRequest userRequest) {


        // 암호화 추가
        User user = User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .name(userRequest.getName())
                .nickname(userRequest.getNickname())
                .phone(userRequest.getPhone())
                .address(userRequest.getAddress())
                .role(UserRole.USER)
                .build();
        authRepository.save(user);
    }

    public int sendEmail(String email) {
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

    private int createAuthNumber() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
