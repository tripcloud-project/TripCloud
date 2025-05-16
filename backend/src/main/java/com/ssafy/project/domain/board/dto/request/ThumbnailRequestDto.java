package com.ssafy.project.domain.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThumbnailRequestDto {
	private Long memberId;
	private Long photoId;
	private String sido;
	private String sigungu;
}
