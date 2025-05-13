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
public class DirectoryPreviewResponseDto {
	private @NonNull String name;
	private @NonNull String prefix;
}
