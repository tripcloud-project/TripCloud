package com.ssafy.project.domain.board.entity;

import com.ssafy.project.domain.common.entity.BaseEntity;

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

public class Post extends BaseEntity{
	private @NonNull Long postId;
	private @NonNull Long memberId;
	private @NonNull String title;
	private @NonNull String content;
	private String filePath;
}
