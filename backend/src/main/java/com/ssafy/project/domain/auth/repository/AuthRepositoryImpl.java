package com.ssafy.project.domain.auth.repository;

import com.ssafy.project.domain.auth.dto.request.LoginRequest;
import com.ssafy.project.domain.auth.dto.response.LoginResponse;
import com.ssafy.project.domain.auth.mapper.AuthMapper;
import com.ssafy.project.domain.member.entity.Member;
import org.springframework.stereotype.Repository;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class AuthRepositoryImpl implements AuthRepository {

    private final AuthMapper authMapper;

    @Override
    public LoginResponse findByEmailAndPassword(LoginRequest loginRequest) {

        Member member = authMapper.selectByEmailAndPassword(loginRequest);
        member.setMemberId(1L);
        System.out.println("member = " + member);

        if(member == null) {
            return null;
        }

        return LoginResponse.builder()
                .memberId(member.getMemberId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }

}
