package com.ssafy.project.domain.auth.service;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.repository.AuthRepository;
import com.ssafy.project.domain.member.exception.NotFoundMemberException;
import com.ssafy.project.util.JWTUtil;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;
    private final AuthRepository authRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // 비밀번호 규칙: 8자 이상, 대문자, 소문자, 숫자, 특수문자
    // -> 정규표현식

    // 회원가입 --> ^[a-zA-Z\\d`~!@#$%^&*()-_=+]{8,24}$

    private boolean isValid(String rawPassword) {

        if(rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        String regex = "^[a-zA-Z\\d`~!@#$%^&*()\\-_=+]{8,24}$";

        RegexValidator validator = new RegexValidator(regex);

        return validator.isValid(rawPassword);

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
    	LoginResponseDto loginResponseDto = authRepository.findByEmail(loginRequestDto.getEmail());
    	
    	// 이메일에 맞는 회원 정보가 없거나 비밀번호 불일치
        if(loginResponseDto == null || !passwordEncoder.matches(loginRequestDto.getPassword(), loginResponseDto.getPassword())) {
            throw new NotFoundMemberException();
        }
        
        loginResponseDto.setAccessToken(
                jwtUtil.generateAccessToken(
                		loginResponseDto.getMemberId(),
                		loginResponseDto.getEmail(),
                        loginResponseDto.getName(),
                        loginResponseDto.getRole()
                )
        );

        loginResponseDto.setRefreshToken(
                jwtUtil.generateRefreshToken(
                		loginResponseDto.getMemberId(),
                		loginResponseDto.getEmail()
                )
        );

        return loginResponseDto;
    }

}
