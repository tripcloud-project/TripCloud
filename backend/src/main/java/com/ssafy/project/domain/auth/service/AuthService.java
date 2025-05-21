package com.ssafy.project.domain.auth.service;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.dto.response.TokenResponseDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
	void logout(String authorization, String refreshToken);
	TokenResponseDto reissueToken(String refreshToken);
}
