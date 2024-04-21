package com.cerpha.cerphaproject.common.security;

import com.cerpha.cerphaproject.cerpha.auth.request.LoginRequest;
import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import com.cerpha.cerphaproject.common.security.jwt.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final String ACCESS_TOKEN_EXPIRATION = "token.access_token.expiration_time";
    private final String REFRESH_TOKEN_EXPIRATION = "token.refresh_token.expiration_time";

    private AuthService authService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthService authService,
                                Environment environment) {
        super(authenticationManager);
        this.authService = authService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequest loginRequest = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        Users user = authService.getUserDetailsByEmail(username);

        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("token.secret").getBytes());

        SecretKey secretKey = Keys.hmacShaKeyFor(secretKeyBytes);

        Instant now = Instant.now();

        String accessToken = Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty(ACCESS_TOKEN_EXPIRATION)))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty(REFRESH_TOKEN_EXPIRATION)))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();

        JwtToken jwtToken = new JwtToken(accessToken, refreshToken);

        response.addHeader("accessToken", accessToken);
        response.addHeader("userId", String.valueOf(user.getId()));
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new ResultDto<>(HttpStatus.OK, jwtToken)));
    }
}
