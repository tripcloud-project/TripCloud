package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.*;
import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.gallery.dto.response.DirectoryPreviewResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FilePreviewResponseDto;
import com.ssafy.project.domain.gallery.mapper.FileMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository{
	private final FileMapper fileMapper;
	
	@Override
	public void insertFiles(List<FileDto> photos, Long memberId) {
		fileMapper.insertFiles(photos, memberId);
	}

	@Override
	public void renameFile(Long photoId, String filename, Long memberId) {
		fileMapper.renameFile(photoId, filename, memberId);
	}

	@Override
	public void renameFiles(String oldPrefix, String newPrefix, Long memberId) {
		fileMapper.renameFiles(oldPrefix, newPrefix, memberId);
	}
	
	@Override
	public FileDetailResponseDto findFileDetailByFileIdAndMemberId(Long photoId, Long memberId) {
		return fileMapper.findFileDetailByFileIdAndMemberId(photoId, memberId);
	}

	@Override
	public List<DirectoryEntry> findDirectoriesByPrefixAndIsDeleted(String prefix, Boolean isDeleted){
		return fileMapper.findDirectoriesByPrefixAndIsDeleted(prefix, isDeleted);
	}

	@Override
	public List<FileEntry> findFilesByPrefixAndIsDeleted(String prefix, Boolean isDeleted){
		return fileMapper.findFilesByPrefixAndIsDeleted(prefix, isDeleted);
	}

	@Override
	public boolean existsByPrefix(String prefix){
		return fileMapper.existsByPrefix(prefix);
	}

	@Override
	public S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByFileIdAndMemberId(Long photoId, Long memberId){
		return fileMapper.findS3KeyAndOriginalFilenameByFileIdAndMemberId(photoId, memberId);
	}

	@Override
	public List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId){
		return fileMapper.findS3KeysAndOriginalFilenamesByPrefixAndMemberId(prefix, memberId);
	}

	@Override
	public void softDeleteFilesByIds(List<Long> photoIdList, Long memberId){
		fileMapper.softDeleteFilesByIds(photoIdList, memberId);
	}

	@Override
	public void softDeleteFilesByPrefixes(List<String> prefixeList, Long memberId){
		fileMapper.softDeleteFilesByPrefixes(prefixeList, memberId);
	}

	@Override
	public void restoreFilesByIds(List<Long> photoIdList, Long memberId){
		fileMapper.restoreFilesByIds(photoIdList, memberId);
	}

	@Override
	public void restoreFilesByPrefixes(List<String> prefixList, Long memberId){
		fileMapper.restoreFilesByPrefixes(prefixList, memberId);
	}

	@Override
	public void deleteFilesByIds(List<Long> photoIdList, Long memberId){
		fileMapper.deleteFilesByIds(photoIdList, memberId);
	}

	@Override
	public void deleteFilesByPrefixes(List<String> prefixList, Long memberId){
		fileMapper.deleteFilesByPrefixes(prefixList, memberId);
	}
	
	@Override
	public List<String> findDirectoriesByPrefixAndMemberIdAndIsDeleted(String prefix, Long memberId, boolean isDeleted){
		return fileMapper.findDirectoriesByPrefixAndMemberIdAndIsDeleted(prefix, memberId, isDeleted);
	}

	@Override
	public List<DirectoryPreviewResponseDto> searchDirectoriesWithKeyword(Long memberId, String keyword) {
		return fileMapper.selectAllDirectoriesByMemberIdAndKeyword(memberId, keyword);
	}

	@Override
	public List<FilePreviewResponseDto> searchFilesWithKeyword(Long memberId, String keyword) {
		return fileMapper.selectAllFilesByMemberIdAndKeyword(memberId, keyword);
	}

	@Override
	public List<FilePreviewResponseDto> findFilesWithHashtag(Long memberId, String hashtag) {
		return fileMapper.selectAllFilesByMemberIdAndHashtag(memberId, hashtag);
	}

	@Override
	public List<FilePreviewResponseDto> findFilesWithDescription(Long memberId, String description) {
		return fileMapper.selectAllFilesByMemberIdAndDescription(memberId, description);
	}

	@Override
	public List<ThumbnailDto> findThumbnailsByMemberId(Long memberId){
		return fileMapper.findThumbnailsByMemberId(memberId);
	}

	@Override
	public Long findFileIdByS3KeyAndMemberId(String s3Key, Long memberId){
		return fileMapper.findFileIdByS3KeyAndMemberId(s3Key, memberId);
	}
}
