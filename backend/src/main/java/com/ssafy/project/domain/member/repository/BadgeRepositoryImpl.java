package com.ssafy.project.domain.member.repository;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;
import com.ssafy.project.domain.member.mapper.BadgeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BadgeRepositoryImpl implements BadgeRepository {
    private final BadgeMapper badgeMapper;
    @Override
    public BadgeResponseDto selectById(Long badgeId) {
        BadgeResponseDto badgeResponseDto = badgeMapper.selectById(badgeId);

        if(badgeResponseDto == null) {
            return new BadgeResponseDto();
        }
        return badgeResponseDto;
    }
}
