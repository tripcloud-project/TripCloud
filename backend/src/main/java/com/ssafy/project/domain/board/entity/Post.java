package com.ssafy.project.domain.board.entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Post {
	private @NonNull Long postId;
	private @NonNull Long memberId;
	private @NonNull String title;
	private @NonNull String content;
	private String filePath;
	private @NonNull LocalDateTime createdAt;
	private @NonNull LocalDateTime modifiedAt;
	private @NonNull Boolean isDeleted;
}
