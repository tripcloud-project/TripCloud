package com.ssafy.project.domain.gallery.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class S3KeyUpdateDto {
    private String oldKey;
    private String newKey;
}
