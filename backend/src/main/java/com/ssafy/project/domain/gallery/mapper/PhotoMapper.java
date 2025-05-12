package com.ssafy.project.domain.gallery.mapper;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PhotoMapper {
    List<DirectoryEntry> findAllSidoByMemberId(Long memberId);
    List<DirectoryEntry> findSigunguBySidoAndMemberId(String sido, Long memberId);
    List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String sido, String sigungu, Long memberId);
}
