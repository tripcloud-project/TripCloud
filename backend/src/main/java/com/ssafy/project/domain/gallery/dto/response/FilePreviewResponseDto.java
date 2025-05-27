package com.ssafy.project.domain.gallery.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.HashtagDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilePreviewResponseDto {
	private @NonNull Long fileId;
	private @NonNull String name;
	
	private String s3Key;
	private Long size;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime takenAt;
    private String contentType;
    private String sido;
    private String sigungu;
    private String eupmyeondong;
    private String latitude;
    private String longitude;
    private String description;
    
    private String presignedUrl;
    private List<HashtagDto> hashtags;
}
