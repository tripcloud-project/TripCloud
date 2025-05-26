package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;
import com.ssafy.project.domain.member.dto.request.MemberUpdateRequestDto;
import com.ssafy.project.domain.member.dto.response.StorageResponseDto;
import com.ssafy.project.domain.member.entity.Member;

import lombok.NonNull;

public interface MemberRepository {
    void insertMember(MemberRegisterDto memberRegisterDto);
    Boolean existsByEmail(@NonNull String email);
    Member selectById(@NonNull Long id);
	void updateMember(Long memberId, MemberUpdateRequestDto requestDto);
	void deleteMember(@NonNull Long memberId);
    StorageResponseDto getStorageByMemberId(Long memberId);
    void updateStorageByMemberId(Long memberId);
}
