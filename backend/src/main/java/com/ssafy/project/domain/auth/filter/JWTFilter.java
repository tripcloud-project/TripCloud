package com.ssafy.project.domain.auth.filter;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.auth.exception.InvalidTokenException;
import com.ssafy.project.domain.auth.repository.RedisRepository;
import com.ssafy.project.exception.ErrorCode;
import com.ssafy.project.util.JWTUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT Authentication Filter
 * 특정 엔드포인트에 대해 JWT 인증 적용
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 필터를 적용하지 않을 URL 패턴 목록
    
    private static final Map<String, List<String>> EXCLUDE_URL = new HashMap<>();
    
    static {
    	EXCLUDE_URL.put("GET", Arrays.asList(
    			"/api/v1/posts",
    			"/api/v1/refresh-token",
    			"/api/v1/member/checkEmail",
    			"/",
    			"/index.html"
    	));
    	EXCLUDE_URL.put("POST", Arrays.asList(
    			"/api/v1/members",
    			"/api/v1/auth/login",
    			"/api/v1/auth/logout"
    	));
    }
    
    /**
     * 특정 요청에 대해 필터를 적용하지 않도록 설정
     */
    @Override
    public boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod().toUpperCase();
        
        List<String> excludedPaths = EXCLUDE_URL.getOrDefault(method, Collections.emptyList());
        // 지정된 URL 패턴이 포함된 경로 제외
        return excludedPaths.stream()
        		.anyMatch(path::contains);
    }

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws IOException {
        // 인증 필요한 경로에 대한 JWT 토큰 검증
        final String authHeader = request.getHeader("Authorization");

        // 인증 헤더 검사
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
            return;
        }

        // 토큰 추출
        final String jwt = authHeader.substring(7);

        try {
            // 토큰 유효성 검증
            jwtUtil.validateToken(jwt);
            
            if(redisRepository.exists("logout: " + jwt))
            	throw new InvalidTokenException();

            Authentication auth = jwtUtil.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 토큰이 유효하면 다음 필터로 진행
            filterChain.doFilter(request, response);

        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException e) {
            log.error("Invalid JWT Token: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            log.error("JWT Token expired: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (InvalidTokenException e) {
            log.error("Black List JWT Token used: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        } catch (Exception e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
            sendErrorResponse(response, ErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 에러 응답을 보내는 메소드
     * ControllerAdvice 스타일로 일관된 에러 포맷 유지
     */
    private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        ApiResponse errorResponse = ApiResponse.createError(errorCode.getMessage());

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        String responseBody = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(responseBody);
    }
}
