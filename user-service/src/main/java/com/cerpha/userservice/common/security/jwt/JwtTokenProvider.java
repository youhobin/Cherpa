package com.cerpha.userservice.common.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String ACCESS_TOKEN_EXPIRATION = "env.token.access_token.expiration_time";
    private final String REFRESH_TOKEN_EXPIRATION = "env.token.refresh_token.expiration_time";
    @Value("${env.token.secret}")
    private String tokenSecret;
    private final Environment environment;
    private final SecretKey secretKey;

    public JwtTokenProvider(Environment environment) {
        this.environment = environment;
        byte[] secretKeyBytes = Base64.getEncoder().encode(environment.getProperty("env.token.secret").getBytes());
        this.secretKey = Keys.hmacShaKeyFor(secretKeyBytes);
    }

    public String generateAccessToken(String userId) {
        Instant now = Instant.now();

        return Jwts.builder()
                .subject(userId)
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty(ACCESS_TOKEN_EXPIRATION)))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken() {
        Instant now = Instant.now();

        return Jwts.builder()
                .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty(REFRESH_TOKEN_EXPIRATION)))))
                .issuedAt(Date.from(now))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
