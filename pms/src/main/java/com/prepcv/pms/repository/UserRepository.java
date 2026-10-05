package com.prepcv.pms.repository;

import com.prepcv.pms.entity.Role;
import com.prepcv.pms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    long countByRole(Role role);

    long countByRoleAndEnabledTrue(Role role);
}