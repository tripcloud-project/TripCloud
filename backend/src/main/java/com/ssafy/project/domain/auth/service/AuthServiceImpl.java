package com.ssafy.project.domain.auth.service;

import org.apache.commons.validator.routines.RegexValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.project.domain.auth.dto.request.LoginRequestDto;
import com.ssafy.project.domain.auth.dto.response.LoginResponseDto;
import com.ssafy.project.domain.auth.dto.response.TokenResponseDto;
import com.ssafy.project.domain.auth.exception.InvalidTokenException;
import com.ssafy.project.domain.auth.repository.AuthRepository;
import com.ssafy.project.domain.auth.repository.RedisRepository;
import com.ssafy.project.domain.member.exception.NotFoundMemberException;
import com.ssafy.project.util.JWTUtil;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final JWTUtil jwtUtil;
    private final AuthRepository authRepository;
    private final RedisRepository redisRepository;

    @Value("${jwt.refresh-token.expiration}")
    private long jwtRefreshTokenExpiration;
    
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    // 비밀번호 규칙: 8자 이상, 대문자, 소문자, 숫자, 특수문자
    // -> 정규표현식

    // 회원가입 --> ^[a-zA-Z\\d`~!@#$%^&*()-_=+]{8,24}$

    private boolean isValid(String rawPassword) {

        if(rawPassword == null || rawPassword.isBlank()) {
            return false;
        }

        String regex = "^[a-zA-Z\\d`~!@#$%^&*()\\-_=+]{8,24}$";

        RegexValidator validator = new RegexValidator(regex);

        return validator.isValid(rawPassword);

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
    	LoginResponseDto loginResponseDto = authRepository.findByEmail(loginRequestDto.getEmail());
    	
    	// 이메일에 맞는 회원 정보가 없거나 비밀번호 불일치
        if(loginResponseDto == null || !passwordEncoder.matches(loginRequestDto.getPassword(), loginResponseDto.getPassword())) {
            throw new NotFoundMemberException();
        }
        
        String accessToken = jwtUtil.generateAccessToken(
        		loginResponseDto.getMemberId(),
        		loginResponseDto.getEmail(),
                loginResponseDto.getName(),
                loginResponseDto.getRole()
        );
        
        String refreshToken = jwtUtil.generateRefreshToken(
        		loginResponseDto.getMemberId(),
        		loginResponseDto.getEmail()
        );
        
        loginResponseDto.setAccessToken(accessToken);
        loginResponseDto.setRefreshToken(refreshToken);
        String memberId = String.valueOf(loginResponseDto.getMemberId());
        
        redisRepository.delete("refresh: " + memberId);
        redisRepository.save("refresh: " + memberId, refreshToken, jwtRefreshTokenExpiration);
  
        return loginResponseDto;
    }

	@Override
	public void logout(String authorization, String refreshToken) {
		String accessToken = jwtUtil.resolveToken(authorization);
		String memberId = jwtUtil.extractId(accessToken);
		
		// refreshToken 삭제
		redisRepository.delete("refresh: " + memberId);
		
		// accessToken 블랙리스트 등록, 7일간
		redisRepository.save("logout: " + accessToken, "logout", 604800000L);
	}

	@Transactional
	@Override
	public TokenResponseDto reissueToken(String refreshToken) {
		// 리프레시 토큰 검증
		jwtUtil.validateToken(refreshToken);
		Claims claim = jwtUtil.extractAllClaims(refreshToken);
		
		Long memberId = claim.get("id", Long.class);
		String email = claim.get("email", String.class);
		
		// 리프레시 토큰이 redis에 있는지 확인
		if(!redisRepository.exists("refresh: " + memberId))
			throw new InvalidTokenException();
		
		// 리프레시 토큰이 유효하다면 redis에서 삭제
		redisRepository.delete("refresh: " + memberId);
		
		// 리프레시 토큰을 재발급하여 redis에 추가
		refreshToken = jwtUtil.generateRefreshToken(memberId, email);
		redisRepository.save("refresh: " + memberId, refreshToken, jwtRefreshTokenExpiration);
		
		// 엑세스 토큰 재발급
    	LoginResponseDto loginResponseDto = authRepository.findByEmail(email);
    	
    	if(loginResponseDto == null)
    		throw new NotFoundMemberException();
    	
    	String accessToken = jwtUtil.generateAccessToken(
        		loginResponseDto.getMemberId(),
        		loginResponseDto.getEmail(),
                loginResponseDto.getName(),
                loginResponseDto.getRole()
        );
		
		// 반환
    	return TokenResponseDto.builder()
    			.accessToken(accessToken)
    			.refreshToken(refreshToken)
    			.build();
	}
}
