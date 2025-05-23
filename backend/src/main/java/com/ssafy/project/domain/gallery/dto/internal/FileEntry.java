package com.ssafy.project.domain.gallery.dto.internal;

import java.time.LocalDateTime;

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
}
