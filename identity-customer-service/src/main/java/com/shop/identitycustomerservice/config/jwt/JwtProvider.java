package com.shop.identitycustomerservice.config.jwt;

import com.shop.identitycustomerservice.exception.AccessDeniedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@Getter
public class JwtProvider {

    private final SecretKey signingKey;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms:300000}") long accessExpirationMs,
            @Value("${jwt.refresh.expiration-ms:604800000}") long refreshExpirationMs
    ) {
        this.signingKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }


    private String generateToken(String email, UUID accountUuid, Instant dateExpiration) {
        return Jwts.builder()
                .id(accountUuid.toString())
                .subject(email)
                .issuedAt(new Date())
                .expiration(Date.from(dateExpiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    private String generateToken(String email, UUID accountUuid, Instant dateExpiration, Claims claims) {
        return Jwts.builder()
                .id(accountUuid.toString())
                .subject(email)
                .issuedAt(new Date())
                .claims(claims)
                .expiration(Date.from(dateExpiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(String email, UUID accountUuid) {
        Instant now = Instant.now();
        return generateToken(email,accountUuid,now.plus(5, ChronoUnit.MINUTES));
    }

    public String generateAccessToken(String email, UUID accountUuid) {
        Instant now = Instant.now();
        return generateToken(email,accountUuid,now.plus(accessExpirationMs, ChronoUnit.MILLIS));
    }

    public String generateRefreshToken(String email, UUID accountUuid) {
        Instant now = Instant.now();
        return generateToken(email,accountUuid,now.plus(refreshExpirationMs, ChronoUnit.MILLIS));
    }

    public String generatePasswordResetToken(String email, UUID accountUuid) {
        Instant now = Instant.now();
        Claims claims = Jwts.claims()
                .add("permission","PASSWORD_RESET")
                .build();
        return generateToken(email,accountUuid,now.plus(5, ChronoUnit.MINUTES),claims);
    }



    public Map<String, String> getUuidFromToken(String token) {
       try{
           Claims claims = getClaimsFromToken(token);
           return Map.of(
                   "uuid", claims.getId(),
                   "email", claims.getSubject()
           );
       } catch (NullPointerException ex){
           throw new JwtException("Invalid token");
       }
    }

    public Map<String, String> getPermissionResetPassword(String token){
        try {
            Claims claims = getClaimsFromToken(token);
            return Map.of(
                    "uuid", claims.getId(),
                    "email", claims.getSubject(),
                    "permission", claims.get("permission", String.class)
            );
        } catch (NullPointerException ex) {
            throw new AccessDeniedException("Token không hợp lệ", HttpStatus.FORBIDDEN);
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
