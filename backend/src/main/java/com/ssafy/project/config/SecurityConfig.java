package com.ssafy.project.config;


import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.project.domain.auth.filter.JWTFilter;

import lombok.RequiredArgsConstructor;


@Configuration // 이 클래스는 설정 파일용 명시
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // 같은 Origin? : URL + PORT
        // CORS: 브라우저 정책 URL + PORT가 다르면? 허가 안됨. --> 그래서 허용
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .csrf(AbstractHttpConfigurer::disable) // CSRF: XSS랑 혼동 X
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Session 사용 안함.
                .authorizeHttpRequests(authorize ->
                        authorize
                        		// TODO: 01-2. Vue 추가시 삭제 필요합니다.
                        		.requestMatchers("/").permitAll()
                        		.requestMatchers("/index.html").permitAll()
                                .requestMatchers("/api/v1/auth/**").permitAll() // /auth/** 경로에 해당하는 모든 요청은 그냥 허용
                                .requestMatchers(HttpMethod.POST, "/api/v1/members").permitAll() // POST /members만 허용
                                .requestMatchers(HttpMethod.GET, "/api/v1/members/checkEmail").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                                .anyRequest().authenticated()
                )
                // TODO: 01-3. Vue 추가시 삭제 필요합니다.
                .exceptionHandling(exception -> 
	                exception.authenticationEntryPoint((request, response, authException) -> {
	                    response.sendRedirect("/"); // 인증 실패 시 이동할 기본 페이지
	                })
    			)
                .formLogin(AbstractHttpConfigurer::disable) // Spring Security 기본 로그인 비활성화.
                .httpBasic(AbstractHttpConfigurer::disable)// Spring Security 인증 메커니즘 비활성화.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    // Preflight 요청 OPTIONS Method: GET, POST, PUT ... 를 확인
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173")); // 특정 출처만 허용
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 자격 증명 허용 (쿠키 등)
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L); // Preflight 요청 결과를 캐시하는 시간 (초)

        // Content-Disposition 추가
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Disposition"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 URL에 위의 CORS 설정 적용

        return source;
    }
}
