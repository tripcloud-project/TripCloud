package com.ssafy.project.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberResponseDto {
    private String email;
    private String role;
    private String name;
    private String profileImage;
    private Integer usedStorage;
    private Integer maxStorage;
    private String mainBadge;
}
