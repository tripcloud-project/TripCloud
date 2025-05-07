package com.ssafy.project.domain.member.service;

import com.ssafy.project.domain.member.dto.response.ValidateEmailResponseDto;
import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.exception.DuplicateMemberException;
import com.ssafy.project.domain.member.exception.InvalidPasswordException;
import com.ssafy.project.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public void createMember(MemberRegisterDto memberRegisterDto) {

        // 이메일이 중복될 경우
        if (memberRepository.existsByEmail(memberRegisterDto.getEmail())) {
            throw new DuplicateMemberException("이미 등록된 이메일입니다.");
        }

        // 비밀번호 조건 미충족시
        // TODO 현재는 검사 안하지만 비밀번호 조건 체크 필요
        if(isValid(memberRegisterDto.getPassword())) {
            throw new InvalidPasswordException("비밀번호는 8~24자의 영문, 숫자, 특수문자를 포함해야 합니다.");
        }

        memberRegisterDto.setPassword(passwordEncoder.encode(memberRegisterDto.getPassword()));

        memberRepository.insertMember(memberRegisterDto);
    }

    @Override
    public ValidateEmailResponseDto isEmailDuplicated(String email) {
        return new ValidateEmailResponseDto(memberRepository.existsByEmail(email));
    }

    private boolean isValid(String rawPassword) {

        if(rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        String regex = "^[a-zA-Z\\d`~!@#$%^&*()\\-_=+]{8,24}$";

        RegexValidator validator = new RegexValidator(regex);

        return false;
//        return validator.isValid(rawPassword);
    }
}
