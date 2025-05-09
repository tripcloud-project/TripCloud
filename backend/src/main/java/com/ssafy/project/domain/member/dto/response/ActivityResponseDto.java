package com.ssafy.project.domain.member.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivityResponseDto {
	private @NonNull String Activity;
	private @NonNull LocalDateTime createdAt;
}
