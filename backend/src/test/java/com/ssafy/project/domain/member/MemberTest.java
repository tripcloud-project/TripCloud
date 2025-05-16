package com.ssafy.project.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.ssafy.project.domain.member.dto.request.MemberRegisterDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberTest {
	@LocalServerPort
	private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    
    // 테스트용 계정 추가
    @Test
    void 싸피계정회원가입() {
        String baseUrl = "http://localhost:" + port + "/api/v1/members";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
            MemberRegisterDto requestDto = new MemberRegisterDto(
                    "ssafy@ssafy.com",
                    "1234",
                    "ssafy"
                    );

            HttpEntity<MemberRegisterDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
        
    }
    
    // 테스트용 계정 추가
    @Test
    void _123계정회원가입() {
        String baseUrl = "http://localhost:" + port + "/api/v1/members";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
            MemberRegisterDto requestDto = new MemberRegisterDto(
                    "123@123",
                    "123",
                    "123"
                    );

            HttpEntity<MemberRegisterDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);
        
    }

    // 테스트용 계정 추가
    @Test
    void 회원가입_요청_테스트() {
        String baseUrl = "http://localhost:" + port + "/api/v1/members";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        for (int i = 1; i <= 30; i++) {
            String num = String.format("%03d", i); // 001, 002, ..., 30
            MemberRegisterDto requestDto = new MemberRegisterDto(
                    num + "@example.com",
                    num,
                    "테스트유저" + num
            );

            HttpEntity<MemberRegisterDto> request = new HttpEntity<>(requestDto, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, request, String.class);

            assertThat(response.getStatusCode())
                .withFailMessage("실패한 사용자 번호: " + num)
                .isEqualTo(HttpStatus.CREATED);
        }
    }

}
