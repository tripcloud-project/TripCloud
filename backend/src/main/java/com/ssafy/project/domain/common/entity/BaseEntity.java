package com.ssafy.project.domain.common.entity;

import java.time.LocalDateTime;

import lombok.NonNull;

public class BaseEntity {
	private @NonNull LocalDateTime createdAt;
	private @NonNull LocalDateTime modifiedAt;
	private @NonNull Boolean isDeleted;
}
