package com.ssafy.project.domain.member.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeListResponseDto {
	private List<BadgeResponseDto> badges;
	private Integer size;
}
