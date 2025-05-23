package com.ssafy.project.domain.board.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponseDto {
    private @NonNull Long commentId;
    private @NonNull String author;
    private @NonNull String content;
    private @NonNull Boolean isMyComment;
    private @NonNull LocalDateTime createdAt;
}
