package com.cerpha.cerphaproject.common.security.jwt;

import com.cerpha.cerphaproject.cerpha.auth.service.AuthService;
import com.cerpha.cerphaproject.cerpha.user.domain.Users;
import com.cerpha.cerphaproject.common.dto.ResultDto;
import com.cerpha.cerphaproject.common.exception.BusinessException;
import com.cerpha.cerphaproject.common.exception.ExceptionResponse;
import com.cerpha.cerphaproject.common.redis.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import static com.cerpha.cerphaproject.common.exception.ExceptionCode.*;

@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final Environment env;
    private final AuthService authService;
    private final RedisService redisService;

    public JwtAuthenticationFilter(Environment env, AuthService authService, RedisService redisService) {
        this.env = env;
        this.authService = authService;
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("JWT 인증 필터");
        String token = resolveToken(request);

        if (token != null && isValidatedToken(token) && !redisService.hasKeyBlackList(token)) {
            log.info("토큰 유효성 검사 통과");
            Claims claims = null;
            try {
                claims = parseClaims(token);
            } catch (RedisConnectionFailureException e) {
                log.error(e.getMessage());
            } catch (Exception e) {
                log.error(e.getMessage());
            }

            Users user = authService.getUserById(Long.valueOf(claims.getSubject()));
            SecurityContextHolder.getContext()
                    .setAuthentication(
                            new UsernamePasswordAuthenticationToken(user.getEmail(), "", new ArrayList<>())
                    );
        }

        chain.doFilter(request, response);
    }

    private Claims parseClaims(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isValidatedToken(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        boolean returnValue = true;

        String subject = null;

        try {
            JwtParser jwtParser = Jwts.parser()
                    .setSigningKey(signingKey)
                    .build();

            subject = jwtParser.parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            returnValue = false;
            throw e;
        }

        if (subject == null || subject.isEmpty()) {
            returnValue = false;
        }

        return returnValue;
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
