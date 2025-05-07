package com.ssafy.project.domain.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data // Getter, Setter를 생성
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 파라미터 생성자
@Builder // 빌터 패턴을 적용
public class LoginResponseDto {
	private @NonNull Long memberId;
	private @NonNull String password;
	private @NonNull String email;
	private @NonNull String name;
	private @NonNull String role;
	
    private String accessToken; // 모든 과정 인증에 사용 (15분 ~ 30분)
    private String refreshToken; // AccessToken이 만료되었을 때, 재발급에 사용 (AccessToken보다 더 긴 만료일)
}
