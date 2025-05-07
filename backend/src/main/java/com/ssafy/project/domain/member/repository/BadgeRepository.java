package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;

public interface BadgeRepository {
    BadgeResponseDto selectById(Long badgeId);
}
