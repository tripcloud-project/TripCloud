package com.ssafy.project.domain.board.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostRequestDto {
    private Long memberId;
    private @NonNull String title;
    private @NonNull String content;
}
