package com.ssafy.project.domain.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberUpdateRequestDto {
	private String password;
	private @NonNull String name;
	private String profileImage;
	private Long mainBadgeId;
}
