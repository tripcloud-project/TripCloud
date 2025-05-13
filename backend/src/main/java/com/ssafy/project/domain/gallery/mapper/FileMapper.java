package com.ssafy.project.domain.gallery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryEntry;
import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import com.ssafy.project.domain.gallery.dto.internal.FileEntry;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyOriginalFilenameDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryPreviewResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FilePreviewResponseDto;

@Mapper
public interface FileMapper {
	void insertFiles(List<FileDto> files, Long memberId);
	void renameFile(Long fileId, String filename, Long memberId);
	void renameFiles(String oldPrefix, String newPrefix, Long memberId);
	FileDetailResponseDto findFileDetailByFileIdAndMemberId(Long fileId, Long memberId);
	List<DirectoryEntry> findDirectoriesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
	List<FileEntry> findFilesByPrefixAndIsDeleted(String prefix, Boolean isDeleted);
	boolean existsByPrefix(String prefix);
	S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByFileIdAndMemberId(Long fileId, Long memberId);
	List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId);
	void softDeleteFilesByIds(List<Long> fileIdList, Long memberId);
	void softDeleteFilesByPrefixes(List<String> prefixList, Long memberId);
	void restoreFilesByIds(List<Long> fileIdList, Long memberId);
	void restoreFilesByPrefixes(List<String> prefixList, Long memberId);
	void deleteFilesByIds(List<Long> fileIdList, Long memberId);
	void deleteFilesByPrefixes(List<String> prefixList, Long memberId);
	List<DirectoryPreviewResponseDto> selectAllDirectoriesByMemberIdAndKeyword(Long memberId, String keyword);
	List<FilePreviewResponseDto> selectAllFilesByMemberIdAndKeyword(Long memberId, String keyword);
}
