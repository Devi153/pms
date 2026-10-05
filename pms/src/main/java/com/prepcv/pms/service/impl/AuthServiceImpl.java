package com.prepcv.pms.service.impl;

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
import com.prepcv.pms.service.AuthService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(
                    "Email already registered"
            );
        }

        User user = new User();

        user.setEmail(email);

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        // IMPORTANT:
        // Public registration always creates STUDENT.
        user.setRole(Role.STUDENT);

        user.setEnabled(true);

        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getUserId(),
                savedUser.getEmail(),
                savedUser.getRole().name(),
                "Registration successful"
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        ));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                "Bearer",
                900
        );
    }
}