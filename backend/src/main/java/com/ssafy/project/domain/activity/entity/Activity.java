package com.ssafy.project.domain.activity.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)

public class Activity{
	private @NonNull Long activityId;
	private @NonNull Long memberId;
	private @NonNull String activity;
	private @NonNull Integer quantity;
	private @NonNull LocalDateTime createdAt;
}
