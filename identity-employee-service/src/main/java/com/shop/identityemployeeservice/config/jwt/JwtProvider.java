package com.shop.identityemployeeservice.config.jwt;

import com.shop.identityemployeeservice.entity.EmployeeAccount;
import com.shop.identityemployeeservice.entity.Role;
import com.shop.identityemployeeservice.exception.AccessDeniedException;
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
import java.util.List;
import java.util.Map;

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


    private String generateToken(EmployeeAccount employeeAccount, Instant dateExpiration) {
        List<String> roles = employeeAccount.getRoles()
                .stream()
                .map(Role::getName)
                .toList();
        return Jwts.builder()
                .id(employeeAccount.getUuid().toString())
                .subject(employeeAccount.getUsername())
                .issuedAt(new Date())
                .claim("roles", roles)
                .expiration(Date.from(dateExpiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    private String generateToken(EmployeeAccount account, Instant dateExpiration, Claims claims) {
        return Jwts.builder()
                .id(account.getUuid().toString())
                .subject(account.getUsername())
                .issuedAt(new Date())
                .claims(claims)
                .expiration(Date.from(dateExpiration))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    public String generateToken(EmployeeAccount account) {
        Instant now = Instant.now();
        return generateToken(account,now.plus(5, ChronoUnit.MINUTES));
    }

    public String generateAccessToken(EmployeeAccount account) {
        Instant now = Instant.now();
        return generateToken(account,now.plus(accessExpirationMs, ChronoUnit.MILLIS));
    }

    public String generateRefreshToken( EmployeeAccount account) {
        Instant now = Instant.now();
        return generateToken(account,now.plus(refreshExpirationMs, ChronoUnit.MILLIS));
    }

    public String generatePasswordResetToken( EmployeeAccount account) {
        Instant now = Instant.now();
        Claims claims = Jwts.claims()
                .add("permission","PASSWORD_RESET")
                .build();
        return generateToken(account,now.plus(5, ChronoUnit.MINUTES),claims);
    }



    public Map<String, String> getUuidFromToken(String token) {
       try{
           Claims claims = getClaimsFromToken(token);
           return Map.of(
                   "uuid", claims.getId(),
                   "username", claims.getSubject()
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
                    "username", claims.getSubject(),
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
