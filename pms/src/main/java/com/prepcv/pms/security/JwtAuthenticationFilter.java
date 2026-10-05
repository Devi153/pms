package com.prepcv.pms.security;

import com.prepcv.pms.entity.User;
import com.prepcv.pms.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // No Authorization header
        if (authHeader == null || authHeader.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization header must contain Bearer token
        if (!authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Extract JWT token
        String token = authHeader.substring(7).trim();

        if (token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {

            // Extract email from JWT
            String email = jwtService.extractUsername(token);

            if (email == null || email.isBlank()) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Check whether authentication already exists
            if (SecurityContextHolder
                    .getContext()
                    .getAuthentication() != null) {

                filterChain.doFilter(request, response);
                return;
            }

            // Find user using email
            User user = userRepository
                    .findByEmail(email)
                    .orElse(null);

            if (user == null) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Check whether user is enabled
            if (!Boolean.TRUE.equals(user.getEnabled())) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Validate JWT
            if (!jwtService.isTokenValid(token, user)) {
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // Get role from database
            String role = user.getRole().name();

            /*
             * Spring Security authority format:
             *
             * ADMIN   -> ROLE_ADMIN
             * STUDENT -> ROLE_STUDENT
             */
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + role);

            /*
             * Use the email as the authenticated principal.
             *
             * This makes:
             *
             * authentication.getName()
             *
             * return the user's email.
             */
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(authority)
                    );

            // Store authentication in SecurityContext
            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

        } catch (Exception exception) {

            // JWT is malformed, expired, invalid, etc.
            SecurityContextHolder.clearContext();

            System.out.println(
                    "JWT authentication failed: "
                            + exception.getMessage()
            );

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Continue request
        filterChain.doFilter(request, response);
    }
}