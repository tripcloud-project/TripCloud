package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;
import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyOriginalFilenameDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;

public interface PhotoRepository {
	void insertPhotos(List<PhotoDto> photos, Long memberId);
	void renamePhoto(Long photoId, String filename, Long memberId);
	PhotoDetailResponseDto findPhotoDetailByPhotoIdAndMemberId(Long photoId, Long memberId);
    List<DirectoryDto> findDirectoriesByPrefix(String prefix);
    List<FileDto> findFilesByPrefix(String prefix);
	void renamePhotos(String oldKey, String newKey);
	S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByPhotoIdAndMemberId(Long photoId, Long memberId);
	List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId);
}
