package com.ssafy.project.domain.attraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttractionResponseDto {
	private String name;
	private String category;
	private String address;
	private String placeURL;
}
