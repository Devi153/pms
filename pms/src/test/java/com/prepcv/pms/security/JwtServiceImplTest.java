package com.prepcv.pms.security;

import com.prepcv.pms.entity.Role;
import com.prepcv.pms.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceImplTest {

    private static final String SECRET =
            "this-is-a-very-long-secret-key-for-testing-purpose-123456";

    private static final long EXPIRATION =
            900000L;

    private JwtServiceImpl jwtService;

    private User user;

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {

        jwtService = new JwtServiceImpl(
                SECRET,
                EXPIRATION
        );

        secretKey = Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8)
        );

        user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setRole(Role.STUDENT);
        user.setEnabled(true);
    }


    // ============================================================
    // 1. TOKEN GENERATION
    // ============================================================

    @Test
    void generateToken_shouldGenerateTokenSuccessfully() {

        String token =
                jwtService.generateToken(user);

        assertNotNull(token);

        assertFalse(
                token.isBlank()
        );
    }


    // ============================================================
    // 2. EMAIL / SUBJECT
    // ============================================================

    @Test
    void generateToken_shouldContainUserEmailAsSubject() {

        String token =
                jwtService.generateToken(user);

        Claims claims =
                parseToken(token);

        assertEquals(
                "student@gmail.com",
                claims.getSubject()
        );
    }


    // ============================================================
    // 3. USER ID CLAIM
    // ============================================================

    @Test
    void generateToken_shouldContainUserIdClaim() {

        String token =
                jwtService.generateToken(user);

        Claims claims =
                parseToken(token);

        assertEquals(
                1,
                claims.get("userId", Integer.class)
        );
    }


    // ============================================================
    // 4. ROLE CLAIM
    // ============================================================

    @Test
    void generateToken_shouldContainRoleClaim() {

        String token =
                jwtService.generateToken(user);

        Claims claims =
                parseToken(token);

        assertEquals(
                "STUDENT",
                claims.get("role", String.class)
        );
    }


    // ============================================================
    // 5. EXPIRATION
    // ============================================================

    @Test
    void generateToken_shouldContainExpiration() {

        String token =
                jwtService.generateToken(user);

        Claims claims =
                parseToken(token);

        assertNotNull(
                claims.getExpiration()
        );

        assertTrue(
                claims.getExpiration()
                        .after(claims.getIssuedAt())
        );
    }


    // ============================================================
    // 6. EXTRACT USERNAME
    // ============================================================

    @Test
    void extractUsername_shouldReturnUserEmail() {

        // Arrange
        String token =
                jwtService.generateToken(user);

        // Act
        String username =
                jwtService.extractUsername(token);

        // Assert
        assertEquals(
                "student@gmail.com",
                username
        );
    }


    // ============================================================
    // 7. VALID TOKEN
    // ============================================================

    @Test
    void isTokenValid_shouldReturnTrue_forValidToken() {

        // Arrange
        String token =
                jwtService.generateToken(user);

        // Act
        boolean result =
                jwtService.isTokenValid(
                        token,
                        user
                );

        // Assert
        assertTrue(result);
    }


    // ============================================================
    // 8. DIFFERENT USER
    // ============================================================

    @Test
    void isTokenValid_shouldReturnFalse_forDifferentUser() {

        // Arrange
        String token =
                jwtService.generateToken(user);

        User anotherUser =
                new User();

        anotherUser.setUserId(2L);

        anotherUser.setEmail(
                "another@gmail.com"
        );

        anotherUser.setRole(
                Role.STUDENT
        );

        anotherUser.setEnabled(true);

        // Act
        boolean result =
                jwtService.isTokenValid(
                        token,
                        anotherUser
                );

        // Assert
        assertFalse(result);
    }


    // ============================================================
    // HELPER METHOD
    // ============================================================

    private Claims parseToken(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}