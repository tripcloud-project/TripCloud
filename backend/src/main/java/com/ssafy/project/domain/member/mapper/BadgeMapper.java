package com.ssafy.project.domain.member.mapper;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BadgeMapper {
    BadgeResponseDto selectById(Long badgeId);
}
