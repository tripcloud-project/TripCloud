package com.ssafy.project.domain.auth.service;

import com.ssafy.project.domain.auth.dto.request.LoginRequest;
import com.ssafy.project.domain.auth.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
}
