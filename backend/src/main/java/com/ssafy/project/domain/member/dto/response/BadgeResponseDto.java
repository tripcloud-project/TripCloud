package com.ssafy.project.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponseDto {
	private @NonNull Long badgeId;
    private @NonNull String name;
    private String description;
    private String image;
}
