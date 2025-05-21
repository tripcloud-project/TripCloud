package com.ssafy.project.domain.attraction.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ssafy.project.domain.attraction.dto.response.AttractionResponseDto;
import com.ssafy.project.infra.kakao.KakaoSearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttractionServiceImpl implements AttractionService{
	private final KakaoSearchService kakaoSearchService;

	@Override
	public List<AttractionResponseDto> getRecommendedSpot(String sido, String sigungu) {
		return kakaoSearchService.SearchRecommendedSpots(sido, sigungu);
	}
}
