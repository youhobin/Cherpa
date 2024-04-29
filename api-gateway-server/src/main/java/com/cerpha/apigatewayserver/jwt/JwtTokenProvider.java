package com.cerpha.apigatewayserver.jwt;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
@Slf4j
public class JwtTokenProvider {

//    @Value("${env.token.access_token.expiration_time}")
//    private Long ACCESS_TOKEN_EXPIRATION;
//
//    @Value("${env.token.refresh_token.expiration_time}")
//    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${env.token.secret}")
    private String secret;


    public boolean isValidatedToken(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secret.getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        try {
            Jwts.parser().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException | ExpiredJwtException |
                UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getSubject(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(secret.getBytes());
        SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        Claims claims = Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
