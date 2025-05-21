package com.ssafy.project.domain.member.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidateEmailResponseDto {
    private @NonNull Boolean isDuplicated;
}
