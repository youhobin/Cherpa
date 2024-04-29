package com.cerpha.userservice.common.security;

import com.cerpha.userservice.cerpha.auth.request.LoginRequest;
import com.cerpha.userservice.cerpha.auth.service.AuthService;
import com.cerpha.userservice.cerpha.user.domain.Users;
import com.cerpha.userservice.common.dto.ResultDto;
import com.cerpha.userservice.common.exception.BusinessException;
import com.cerpha.userservice.common.redis.RedisService;
import com.cerpha.userservice.common.security.jwt.JwtToken;
import com.cerpha.userservice.common.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.cerpha.userservice.common.exception.ExceptionCode.INVALID_CREDENTIALS;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthService authService;
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthService authService,
                                RedisService redisService,
                                JwtTokenProvider jwtTokenProvider) {
        super(authenticationManager);
        this.authService = authService;
        this.redisService = redisService;
        this.jwtTokenProvider = jwtTokenProvider;
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
                                            Authentication authResult) throws IOException {
        String username = ((User) authResult.getPrincipal()).getUsername();
        Users user = authService.getUserDetailsByEmail(username);

        String accessToken = jwtTokenProvider.generateAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtTokenProvider.generateRefreshToken();

        redisService.saveRefreshToken(String.valueOf(user.getId()), refreshToken);

        JwtToken jwtToken = new JwtToken(accessToken, refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(new ResultDto<>(HttpStatus.OK, jwtToken)));
    }
}
