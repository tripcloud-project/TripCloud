package com.ssafy.project.domain.auth.repository;

import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;

public interface AuthRepository {
    LoginResponseDto findByEmail(String email);
}
