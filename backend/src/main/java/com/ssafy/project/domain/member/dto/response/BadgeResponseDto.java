package com.ssafy.project.domain.member.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadgeResponseDto {
    private @NonNull String name;
    private String description;
    private String image;
}
