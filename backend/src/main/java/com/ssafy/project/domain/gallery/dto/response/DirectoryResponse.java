package com.ssafy.project.domain.gallery.dto.response;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectoryResponse {
    private String prefix;
    private long totalSize; // 현재 디렉토리 내 총 파일 크기
    private List<DirectoryDto> entries;
}

