package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyOriginalFilenameDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryPreviewResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FilePreviewResponseDto;

public interface FileRepository {
	void insertFiles(List<FileDto> files, Long memberId);
	void renameFile(Long fileId, String filename, Long memberId);
	FileDetailResponseDto findFileDetailByFileIdAndMemberId(Long fileId, Long memberId);
    List<DirectoryEntry> findDirectoriesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
    List<FileEntry> findFilesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
	void renameFiles(String oldPrefix, String newPrefix, Long memberId);
	boolean existsByPrefix(String prefix);
	S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByFileIdAndMemberId(Long fileId, Long memberId);
	List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId);
	void softDeleteFilesByIds(List<Long> fileIdList, Long memberId);
	void softDeleteFilesByPrefixes(List<String> prefixeList, Long memberId);
	void restoreFilesByIds(List<Long> fileIdList, Long memberId);
	void restoreFilesByPrefixes(List<String> prefixList, Long memberId);
	void deleteFilesByIds(List<Long> fileIdList, Long memberId);
	void deleteFilesByPrefixes(List<String> prefixList, Long memberId);
	List<String> findDirectoriesByPrefixAndMemberId(String prefix, Long memberId);
	List<DirectoryPreviewResponseDto> searchDirectoriesWithKeyword(Long memberId, String keyword);
	List<FilePreviewResponseDto> searchFilesWithKeyword(Long memberId, String keyword);
	List<FilePreviewResponseDto> findFilesWithHashtag(Long memberId, String hashtag);
}
