package com.ssafy.project.domain.board.entity;

import java.time.LocalDateTime;

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

public class PostComment extends BaseEntity {
	private @NonNull Long postCommentId;
	private @NonNull Long memberId;
	private @NonNull String postId;
	private @NonNull String content;
}
