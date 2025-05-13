package com.ssafy.project.domain.gallery.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResultResponseDto {
	private List<DirectoryPreviewResponseDto> directories;
	private List<FilePreviewResponseDto> files;
}
