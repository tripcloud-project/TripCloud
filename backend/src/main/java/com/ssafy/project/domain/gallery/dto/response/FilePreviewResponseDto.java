package com.ssafy.project.domain.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePreviewResponseDto {
	private @NonNull Long fileId;
	private @NonNull String name;
}
