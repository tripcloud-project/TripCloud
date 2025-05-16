package com.ssafy.project.domain.attraction.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.domain.attraction.service.AttractionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attraction")
public class AttractionController {
	private final AttractionService attractionService;
	
	@GetMapping
	public ResponseEntity<?> getRecommendedSpots(@RequestParam String sido, @RequestParam(required = false) String sigungu){
		return ResponseEntity.status(200)
				.body(createSuccess(attractionService.getRecommendedSpot(sido, sigungu)));
	}
}
