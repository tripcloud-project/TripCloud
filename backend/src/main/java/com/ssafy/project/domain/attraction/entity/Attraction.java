package com.ssafy.project.domain.attraction.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attraction {
	private @NonNull Long attractionId;
	private @NonNull String attractionCategoryName;
	private @NonNull String sido;
	private @NonNull String sigungu;
	private @NonNull String eupmyeondong;
	private @NonNull String name;
	private @NonNull String description;
	private @NonNull String latitude;
	private @NonNull String longitude;
}
