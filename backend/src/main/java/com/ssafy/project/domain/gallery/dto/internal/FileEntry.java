package com.ssafy.project.domain.gallery.dto.internal;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntry {
    private Long fileId;
    private String s3Key;
    private String name;
    private Long size;
    private String presignedUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime takenAt;

    private String contentType;
    private String sido;       // 시/도
    private String sigungu;    // 시/군/구
    private String eupmyeondong; // 읍/면/동
    private String latitude;
    private String longitude;
    private String description;
    private List<HashtagDto> hashtags;
}
