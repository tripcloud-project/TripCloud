package com.ssafy.project.domain.gallery.dto.response;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.RegionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhotoStructureResponseDto {
	List<RegionDto> directories;
}
