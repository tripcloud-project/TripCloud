package com.ssafy.project.domain.member.mapper;

import com.ssafy.project.domain.member.dto.response.StorageResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.ssafy.project.domain.member.entity.Member;

import lombok.NonNull;

@Mapper
public interface MemberMapper {
    int insert(MemberRegisterDto memberRegisterDto);
    boolean existsByEmail(String email);
    Member selectById(Long id);
	int update(Long memberId, @Param("requestDto") MemberUpdateRequestDto requestDto);
	int delete(@NonNull Long memberId);
    StorageResponseDto getStorageByMemberId(Long memberId);
}
