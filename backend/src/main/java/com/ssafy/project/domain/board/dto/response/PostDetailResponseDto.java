package com.ssafy.project.domain.board.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDetailResponseDto {
	private @NonNull Long postId;
	private @NonNull String title;
	private @NonNull String content;
	private @NonNull String author;
	private @NonNull LocalDateTime createdAt;
	private @NonNull LocalDateTime modifiedAt;
	private @NonNull Integer likeCount;
	private boolean isMyPost;
	private List<CommentResponseDto> comments;
}
