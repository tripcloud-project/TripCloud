package com.ssafy.project.domain.gallery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.board.dto.request.ThumbnailRequestDto;
import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.internal.RegionDto;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;

@Mapper
public interface PhotoMapper {
    List<DirectoryEntry> findAllSidoByMemberId(Long memberId);
    List<DirectoryEntry> findSigunguBySidoAndMemberId(String sido, Long memberId);
    List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String sido, String sigungu, Long memberId);
	int updateDescription(PhotoDescriptionRequestDto requestDto);
	FileDetailResponseDto selectByMemberIdAndPhotoId(Long memberId, Long photoId);
	int updateThumbnail(ThumbnailRequestDto requestDto);
	List<String> findDistinctSidoByMemberId(Long memberId);
	List<String> findDistinctSigunguBySidoAndMemberId(String sido, Long memberId);
}
