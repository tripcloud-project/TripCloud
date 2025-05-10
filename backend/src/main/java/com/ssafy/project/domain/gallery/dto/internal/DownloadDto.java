package com.ssafy.project.domain.gallery.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadDto {
    private Resource resource;
    private String contentDisposition;
}
