package com.ssafy.project.domain.attraction.service;

import java.util.List;

import com.ssafy.project.domain.attraction.dto.response.AttractionResponseDto;

public interface AttractionService {
	List<AttractionResponseDto> getRecommendedSpot(String sido, String sigungu);
}
