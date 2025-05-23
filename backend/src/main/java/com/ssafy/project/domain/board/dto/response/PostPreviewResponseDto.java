package com.ssafy.project.domain.board.dto.response;

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
public class PostPreviewResponseDto {
	private @NonNull Long postId;
	private @NonNull String title;
	private @NonNull String author;
	private Integer commentCount;
	private @NonNull String profilePresignedURL;
	private @NonNull LocalDateTime createdAt;
}
