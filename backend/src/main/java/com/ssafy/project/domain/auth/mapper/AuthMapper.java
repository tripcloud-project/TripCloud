package com.ssafy.project.domain.auth.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;


@Mapper
public interface AuthMapper {
    LoginResponseDto selectByEmail(String email);
}
