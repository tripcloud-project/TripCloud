package com.ssafy.project.domain.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThumbnailResponseDto {
	private String sido;
	private String sigungu;
	private String s3Key;
}
