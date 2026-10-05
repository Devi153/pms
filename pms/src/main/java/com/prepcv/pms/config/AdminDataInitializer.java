package com.prepcv.pms.config;

import com.prepcv.pms.entity.Role;
import com.prepcv.pms.entity.User;
import com.prepcv.pms.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminDataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            String adminEmail = "admin@prepcv.com";

            if (userRepository.findByEmail(adminEmail).isEmpty()) {

                User admin = new User();

                admin.setEmail(adminEmail);

                admin.setPasswordHash(
                        passwordEncoder.encode("Admin@12345")
                );

                admin.setRole(Role.ADMIN);

                admin.setEnabled(true);

                userRepository.save(admin);

                System.out.println(
                        "Admin account created: " + adminEmail
                );
            }
        };
    }
}