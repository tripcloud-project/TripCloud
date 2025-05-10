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
    List<DirectoryDto> findDirectoriesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
    List<FileDto> findFilesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
	void renamePhotos(String oldPrefix, String newPrefix, Long memberId);
	boolean existsByPrefix(String prefix);
	S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByPhotoIdAndMemberId(Long photoId, Long memberId);
	List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId);
	void softDeletePhotosByIds(List<Long> photoIdList, Long memberId);
	void softDeletePhotosByPrefixes(List<String> prefixeList, Long memberId);
	void restorePhotosByIds(List<Long> photoIdList, Long memberId);
	void restorePhotosByPrefixes(List<String> prefixList, Long memberId);
	void deletePhotosByIds(List<Long> photoIdList, Long memberId);
	void deletePhotosByPrefixes(List<String> prefixList, Long memberId);
}
