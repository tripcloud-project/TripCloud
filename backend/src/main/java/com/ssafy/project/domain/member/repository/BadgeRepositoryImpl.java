package com.ssafy.project.domain.member.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.member.dto.response.BadgeResponseDto;
import com.ssafy.project.domain.member.mapper.BadgeMapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    
	@Override
	public List<BadgeResponseDto> selectAllByMemberId(@NonNull Long memberId) {
		return badgeMapper.selectAllByMemberId(memberId);
	}

	@Override
	public Boolean existsByMemberIdAndBadgeId(@NonNull Long memberId, Long badgeId) {
		return badgeMapper.existsByMemberIdAndBadgeId(memberId, badgeId);
	}
}
