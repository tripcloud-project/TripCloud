package com.ssafy.project.domain.member.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.member.dto.response.ActivityResponseDto;

@Mapper
public interface ActivityMapper {
	List<ActivityResponseDto> selectByMemberIdBeforeCursor(Long memberId, LocalDateTime cursor, Integer size);
}
