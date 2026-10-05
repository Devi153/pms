package com.prepcv.pms.service;

import com.prepcv.pms.dto.LoginRequest;
import com.prepcv.pms.dto.LoginResponse;
import com.prepcv.pms.dto.RegisterRequest;
import com.prepcv.pms.dto.RegisterResponse;
import com.prepcv.pms.entity.Role;
import com.prepcv.pms.entity.User;
import com.prepcv.pms.exception.EmailAlreadyExistsException;
import com.prepcv.pms.exception.InvalidCredentialsException;
import com.prepcv.pms.repository.UserRepository;
import com.prepcv.pms.security.JwtService;
import com.prepcv.pms.service.impl.AuthServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest(
                "student@gmail.com",
                "password123"
        );
    }


    // ============================================================
    // REGISTER TESTS
    // ============================================================

    @Test
    void register_shouldCreateStudentSuccessfully() {

        // Arrange
        when(userRepository.findByEmail(
                registerRequest.getEmail()
        )).thenReturn(Optional.empty());

        when(passwordEncoder.encode(
                registerRequest.getPassword()
        )).thenReturn("hashedPassword");

        User savedUser = new User();

        savedUser.setUserId(1L);
        savedUser.setEmail("student@gmail.com");
        savedUser.setPasswordHash("hashedPassword");
        savedUser.setRole(Role.STUDENT);
        savedUser.setEnabled(true);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        RegisterResponse response =
                authService.register(registerRequest);

        // Assert
        assertNotNull(response);

        assertEquals(
                1L,
                response.getUserId()
        );

        assertEquals(
                "student@gmail.com",
                response.getEmail()
        );

        assertEquals(
                "STUDENT",
                response.getRole()
        );

        assertEquals(
                "Registration successful",
                response.getMessage()
        );

        verify(userRepository)
                .findByEmail(registerRequest.getEmail());

        verify(passwordEncoder)
                .encode(registerRequest.getPassword());

        verify(userRepository)
                .save(any(User.class));
    }


    @Test
    void register_shouldThrowException_whenEmailAlreadyExists() {

        // Arrange
        User existingUser = new User();

        existingUser.setEmail(
                registerRequest.getEmail()
        );

        when(userRepository.findByEmail(
                registerRequest.getEmail()
        )).thenReturn(Optional.of(existingUser));

        // Act & Assert
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> authService.register(registerRequest)
        );

        verify(userRepository, never())
                .save(any(User.class));

        verify(passwordEncoder, never())
                .encode(anyString());
    }


    @Test
    void register_shouldEncodePassword() {

        // Arrange
        when(userRepository.findByEmail(
                registerRequest.getEmail()
        )).thenReturn(Optional.empty());

        when(passwordEncoder.encode(
                "password123"
        )).thenReturn("bcryptHash");

        User savedUser = new User();

        savedUser.setUserId(1L);
        savedUser.setEmail("student@gmail.com");
        savedUser.setPasswordHash("bcryptHash");
        savedUser.setRole(Role.STUDENT);
        savedUser.setEnabled(true);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        authService.register(registerRequest);

        // Assert
        verify(passwordEncoder)
                .encode("password123");
    }


    @Test
    void register_shouldAlwaysAssignStudentRole() {

        // Arrange
        when(userRepository.findByEmail(
                registerRequest.getEmail()
        )).thenReturn(Optional.empty());

        when(passwordEncoder.encode(anyString()))
                .thenReturn("bcryptHash");

        User savedUser = new User();

        savedUser.setUserId(1L);
        savedUser.setEmail("student@gmail.com");
        savedUser.setPasswordHash("bcryptHash");
        savedUser.setRole(Role.STUDENT);
        savedUser.setEnabled(true);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        authService.register(registerRequest);

        // Assert
        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository)
                .save(userCaptor.capture());

        User capturedUser =
                userCaptor.getValue();

        assertEquals(
                Role.STUDENT,
                capturedUser.getRole()
        );
    }


    @Test
    void register_shouldNormalizeEmail() {

        // Arrange
        RegisterRequest request =
                new RegisterRequest(
                        "  Student@GMAIL.COM  ",
                        "password123"
                );

        when(userRepository.findByEmail(
                "student@gmail.com"
        )).thenReturn(Optional.empty());

        when(passwordEncoder.encode(
                "password123"
        )).thenReturn("bcryptHash");

        User savedUser = new User();

        savedUser.setUserId(1L);
        savedUser.setEmail("student@gmail.com");
        savedUser.setPasswordHash("bcryptHash");
        savedUser.setRole(Role.STUDENT);
        savedUser.setEnabled(true);

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act
        RegisterResponse response =
                authService.register(request);

        // Assert
        assertEquals(
                "student@gmail.com",
                response.getEmail()
        );

        verify(userRepository)
                .findByEmail("student@gmail.com");
    }


    // ============================================================
    // LOGIN TESTS
    // ============================================================

    @Test
    void login_shouldReturnJwtSuccessfully() {

        // Arrange
        LoginRequest request =
                new LoginRequest(
                        "student@gmail.com",
                        "password123"
                );

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPasswordHash("bcryptHash");
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        when(userRepository.findByEmail(
                "student@gmail.com"
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "password123",
                "bcryptHash"
        )).thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        // Act
        LoginResponse response =
                authService.login(request);

        // Assert
        assertNotNull(response);

        assertEquals(
                "jwt-token",
                response.getAccessToken()
        );

        assertEquals(
                "Bearer",
                response.getTokenType()
        );

        assertEquals(
                900,
                response.getExpiresIn()
        );

        verify(userRepository)
                .findByEmail("student@gmail.com");

        verify(passwordEncoder)
                .matches(
                        "password123",
                        "bcryptHash"
                );

        verify(jwtService)
                .generateToken(user);
    }


    @Test
    void login_shouldThrowException_whenEmailDoesNotExist() {

        // Arrange
        LoginRequest request =
                new LoginRequest(
                        "unknown@gmail.com",
                        "password123"
                );

        when(userRepository.findByEmail(
                "unknown@gmail.com"
        )).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(any(User.class));
    }


    @Test
    void login_shouldThrowException_whenPasswordIsWrong() {

        // Arrange
        LoginRequest request =
                new LoginRequest(
                        "student@gmail.com",
                        "wrongPassword"
                );

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPasswordHash("bcryptHash");
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        when(userRepository.findByEmail(
                "student@gmail.com"
        )).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "wrongPassword",
                "bcryptHash"
        )).thenReturn(false);

        // Act & Assert
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(jwtService, never())
                .generateToken(any(User.class));
    }


    @Test
    void login_shouldThrowException_whenUserIsDisabled() {

        // Arrange
        LoginRequest request =
                new LoginRequest(
                        "student@gmail.com",
                        "password123"
                );

        User user = new User();

        user.setUserId(1L);
        user.setEmail("student@gmail.com");
        user.setPasswordHash("bcryptHash");
        user.setRole(Role.STUDENT);
        user.setEnabled(false);

        when(userRepository.findByEmail(
                "student@gmail.com"
        )).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(request)
        );

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtService, never())
                .generateToken(any(User.class));
    }
}