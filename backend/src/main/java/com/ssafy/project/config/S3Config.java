package com.ssafy.project.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
	// 내부 접근용 (Java 코드, S3Client)
    @Value("${minio.endpoint}")
    private String endpoint;
    
    // 외부 노출용 Presigned URL (도메인 기반 or 외부에서 접근 가능한 IP)
    @Value("${minio.endpoint.public}")
    private String endpointPublic;

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
    
    @Bean
    S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .endpointOverride(URI.create(endpointPublic)) // MinIO endpoint
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .region(Region.US_EAST_1)
                .serviceConfiguration(
                        S3Configuration.builder()
                            .pathStyleAccessEnabled(true) // << ✅ path-style URL 사용
                            .build()
                    )
                .build();
    }

}