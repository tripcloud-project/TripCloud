package com.ssafy.project.domain.gallery.entity;

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
public class Directory extends BaseEntity{
	private @NonNull Long directoryId;
	private @NonNull String name;
	private @NonNull String fildPath;
}
