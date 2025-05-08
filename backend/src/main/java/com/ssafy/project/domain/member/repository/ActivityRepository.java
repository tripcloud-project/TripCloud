package com.ssafy.project.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.project.domain.member.dto.response.ActivityResponseDto;

public interface ActivityRepository {
	List<ActivityResponseDto> selectByMemberIdAfterCursor(Long memberId, LocalDateTime cursor, Integer size);
}
