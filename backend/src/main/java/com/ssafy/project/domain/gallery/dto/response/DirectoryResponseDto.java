package com.ssafy.project.domain.gallery.dto.response;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;

import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DirectoryResponseDto {
    private String prefix;
    private Long totalSize; // 현재 디렉토리 내 총 파일 크기
    private List<DirectoryDto> directories;
    private List<FileDto> files; // 파일만
}

