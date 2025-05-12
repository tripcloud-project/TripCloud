package com.ssafy.project.domain.gallery.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileDetailResponseDto {
    private String memberName;
    private String originalFilename;
    private Long size;
    private String contentType;
    private String sido;       // 시/도
    private String sigungu;    // 시/군/구
    private String eupmyeondong; // 읍/면/동
    private Double latitude;
    private Double longitude;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime takenAt;
}
