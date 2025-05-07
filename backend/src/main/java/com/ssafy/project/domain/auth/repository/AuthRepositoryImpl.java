package com.ssafy.project.domain.auth.repository;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.mapper.AuthMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthMapper authMapper;

    @Override
    public LoginResponseDto findByEmail(String email) {

        LoginResponseDto loginResponseDto = authMapper.selectByEmail(email);

        if(loginResponseDto == null) {
            return null;
        }

        return loginResponseDto;
    }

}
