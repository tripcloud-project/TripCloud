package com.ssafy.project.domain.member.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.member.dto.response.ActivityResponseDto;
import com.ssafy.project.domain.member.mapper.ActivityMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepository{
	private final ActivityMapper activityMapper;
	@Override
	public List<ActivityResponseDto> selectByMemberIdAfterCursor(Long memberId, LocalDateTime cursor, Integer size) {
		return activityMapper.selectByMemberIdBeforeCursor(memberId, cursor, size);
	}
}
