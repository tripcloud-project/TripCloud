package com.ssafy.project.domain.gallery.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhotoDescriptionRequestDto {
	private @NonNull String description;
	private Long memberId;
	private Long fileId;
}
