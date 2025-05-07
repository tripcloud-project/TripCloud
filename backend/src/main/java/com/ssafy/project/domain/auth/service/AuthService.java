package com.ssafy.project.domain.auth.service;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequestDto loginRequestDto);
}
