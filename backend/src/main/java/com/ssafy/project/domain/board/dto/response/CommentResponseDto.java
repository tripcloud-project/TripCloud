package com.ssafy.project.domain.board.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private @NonNull Long commentId;
    private @NonNull String author;
    private @NonNull String content;
    private @NonNull Boolean isMyComment;
    private String profile;
    private @NonNull LocalDateTime createdAt;
}
