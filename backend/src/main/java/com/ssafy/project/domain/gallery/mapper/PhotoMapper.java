package com.ssafy.project.domain.gallery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;

@Mapper
public interface PhotoMapper {
    List<DirectoryEntry> findAllSidoByMemberId(Long memberId);
    List<DirectoryEntry> findSigunguBySidoAndMemberId(String sido, Long memberId);
    List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String sido, String sigungu, Long memberId);
	int updateDescription(PhotoDescriptionRequestDto requestDto);
}
