package com.ssafy.project.domain.member.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberRegisterDto {
    private @NonNull String email;
    private @NonNull String password;
    private @NonNull String name;
}
