package com.ssafy.project.domain.auth.service;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
	void logout(String authorization, String refreshToken);
}
