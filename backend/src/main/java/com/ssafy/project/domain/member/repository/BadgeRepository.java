package com.ssafy.project.domain.member.repository;

import java.util.List;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;

import lombok.NonNull;

public interface BadgeRepository {
    BadgeResponseDto selectById(Long badgeId);
	List<BadgeResponseDto> selectAllByMemberId(@NonNull Long memberId);
	Boolean existsByMemberIdAndBadgeId(@NonNull Long memberId, Long mainBadgeId);
}
