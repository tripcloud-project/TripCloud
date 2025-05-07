package com.ssafy.project.domain.member.entity;

import com.ssafy.project.domain.common.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class Member extends BaseEntity{
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