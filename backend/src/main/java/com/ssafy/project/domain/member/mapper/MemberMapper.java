package com.ssafy.project.domain.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.ssafy.project.domain.member.entity.Member;

@Mapper
public interface MemberMapper {
    int insert(MemberRegisterDto memberRegisterDto);
    boolean existsByEmail(String email);
    Member selectById(Long id);
	int update(Long memberId, @Param("requestDto") MemberUpdateRequestDto requestDto);
}
