package com.prepcv.pms.service;

import com.prepcv.pms.dto.LoginRequest;
import com.prepcv.pms.dto.LoginResponse;
import com.prepcv.pms.dto.RegisterRequest;
import com.prepcv.pms.dto.RegisterResponse;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}