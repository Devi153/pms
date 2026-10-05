package com.prepcv.pms.security;

import com.prepcv.pms.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final SecretKey secretKey;
    private final long expirationMillis;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expirationMillis) {

        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException(
                    "JWT secret must not be empty"
            );
        }

        this.secretKey = Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );

        this.expirationMillis = expirationMillis;
    }

    @Override
    public String generateToken(User user) {

        Instant now = Instant.now();

        Instant expiration =
                now.plusMillis(expirationMillis);

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getUserId())
                .claim("role", user.getRole().name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiration))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String extractUsername(String token) {

        Claims claims = parseToken(token);

        return claims.getSubject();
    }

    @Override
    public boolean isTokenValid(
            String token,
            User user) {

        Claims claims = parseToken(token);

        String username = claims.getSubject();

        Date expiration = claims.getExpiration();

        if (username == null || expiration == null) {
            return false;
        }

        return username.equals(user.getEmail())
                && expiration.after(new Date());
    }

    private Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
