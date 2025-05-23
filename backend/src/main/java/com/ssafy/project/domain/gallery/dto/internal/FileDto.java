package com.ssafy.project.domain.gallery.dto.internal;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDto {
    private Long fileId;
    private String s3Key;
    private String originalFilename;
    private Long size;
    private String contentType;
    private String sido;       // 시/도
    private String sigungu;    // 시/군/구
    private String eupmyeondong; // 읍/면/동
    private String latitude;
    private String longitude;
    private Boolean isDeleted;
    private LocalDateTime deletedAt;
    private LocalDateTime takenAt;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    
    // 멤버 id 외래키로 추가 예정
}