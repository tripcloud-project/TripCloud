package com.ssafy.project.domain.auth.repository;

import com.ssafy.project.domain.auth.dto.request.LoginRequest;
import com.ssafy.project.domain.auth.dto.response.LoginResponse;

public interface AuthRepository {
    LoginResponse findByEmailAndPassword(LoginRequest loginRequest);
}
