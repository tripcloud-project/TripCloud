package com.ssafy.project.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data // Getter, Setter
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 파라미터 생성자
@Builder // 빌더 패턴
public class LoginRequest {
    private @NonNull String email;
    private @NonNull String password;
}



