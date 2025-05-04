package com.ssafy.project.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {
    @Value("${minio.endpoint}")
    private String endpoint;

    @Value("${minio.access-key}")
    private String accessKey;

    @Value("${minio.secret-key}")
    private String secretKey;

    /**
     * MinIO (또는 AWS S3 호환)용 S3Client Bean 등록
     * 애플리케이션 전역에서 DI(의존성 주입)하여 사용 가능
     */
    @Bean
    S3Client s3Client() {
        return S3Client.builder()
    		// MinIO 서버의 엔드포인트 지정
            .endpointOverride(URI.create(endpoint)) // MinIO 서버 URL
            // 접근 키, 시크릿 키 설정
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(accessKey, secretKey)
                )
            )
            // AWS SDK에서 요구하는 Region (MinIO에서는 아무 Region이나 사용 가능)
            .region(Region.US_EAST_1)
            // S3 호환 서버(MinIO)는 path-style URL 방식 사용 필요
            .forcePathStyle(true)
            .build();
    }
}