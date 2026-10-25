package com.shop.identitycustomerservice.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@Getter
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-ms:900000}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh.expiration-ms:604800000}")
    public long jwtRefreshExpirationMs;


    private String generateToken(String email, UUID accountUuid, Date dateExpiration) {
        SecretKey key= Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
        return Jwts.builder()
                .id(accountUuid.toString())
                .subject(email)
                .issuedAt(new Date())
                .expiration(dateExpiration)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateAccessToken(String email, UUID accountUuid) {
        Date dateExpiration = new Date(new Date().getTime() + jwtExpirationMs);
        return generateToken(email,accountUuid,dateExpiration);
    }

    public String generateRefreshToken(String email, UUID accountUuid) {
        Date dateExpiration = new Date(new Date().getTime() + jwtRefreshExpirationMs);
        return generateToken(email,accountUuid,dateExpiration);
    }


    public Map<String, String> getUuidFromToken(String token) {
        SecretKey key= Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();


        return Map.of(
                "uuid", claims.getId(),
                "email", claims.getSubject()
        );
    }
}
