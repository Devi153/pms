package com.prepcv.pms.security;

import com.prepcv.pms.entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);
}