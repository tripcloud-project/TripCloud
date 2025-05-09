package com.ssafy.project.domain.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;

@Mapper
public interface BadgeMapper {
    BadgeResponseDto selectById(Long badgeId);
    List<BadgeResponseDto> selectAllByMemberId(Long memberId);
}
