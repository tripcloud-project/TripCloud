package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.board.dto.request.ThumbnailRequestDto;
import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;

public interface PhotoRepository {
    List<DirectoryEntry> findAllSidoByMemberId(Long memberId);
    List<DirectoryEntry> findSigunguBySidoAndMemberId(String province, Long memberId);
    List<FileEntry> findPhotosBySidoAndSigunguAndMemberId(String province, String city, Long memberId);
	boolean updateDescription(PhotoDescriptionRequestDto requestDto);
	FileDetailResponseDto findPhotoByPhotoId(Long memberId, Long photoId);
	boolean setThumbnail(ThumbnailRequestDto requestDto);
}
