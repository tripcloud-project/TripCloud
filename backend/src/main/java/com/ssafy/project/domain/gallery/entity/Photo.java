package com.ssafy.project.domain.gallery.entity;

import java.time.LocalDateTime;

import com.ssafy.project.domain.common.entity.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper=false)
public class Photo extends BaseEntity{
	private @NonNull Long photoId;
	private @NonNull Long memberId;
	private @NonNull Long directoryId;
	private @NonNull Long eupmyeondongId;
	private @NonNull String name;
	private @NonNull String description;
	private @NonNull String latitude;
	private @NonNull String longitude;
	private @NonNull String filePath;
	private @NonNull LocalDateTime takenAt;
}
