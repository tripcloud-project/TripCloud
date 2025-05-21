package com.ssafy.project.domain.gallery.dto.internal;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegionDto {
	private String sido;
	private List<String> sigungu;
}
