package com.ssafy.project.domain.gallery.dto.internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DirectoryDto {
    private String name;
    private boolean directory;
    private long size; // byte 단위
}
