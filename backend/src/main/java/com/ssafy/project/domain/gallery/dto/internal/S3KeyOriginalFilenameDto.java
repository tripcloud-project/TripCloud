package com.ssafy.project.domain.gallery.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3KeyOriginalFilenameDto {
    private String s3Key;
    private String originalFilename;
}
