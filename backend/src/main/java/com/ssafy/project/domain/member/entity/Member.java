package com.ssafy.project.domain.member.entity;

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
public class Member {
	private @NonNull Long memberId;
	private @NonNull String email;
	private @NonNull String password;
	private @NonNull String role;
	private @NonNull String name;
	private String profileImage;
	private @NonNull Integer usedStorage;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private Boolean isDeleted;
	private Long mainBadgeId;
	private Integer maxStorage;
}