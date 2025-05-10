package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;
import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyOriginalFilenameDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;
import com.ssafy.project.domain.gallery.mapper.PhotoMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository{
	private final PhotoMapper photoMapper;
	
	@Override
	public void insertPhotos(List<PhotoDto> photos, Long memberId) {
		photoMapper.insertPhotos(photos, memberId);
	}

	@Override
	public void renamePhoto(Long photoId, String filename, Long memberId) {
		photoMapper.renamePhoto(photoId, filename, memberId);
	}

	@Override
	public void renamePhotos(String oldPrefix, String newPrefix, Long memberId) {
		photoMapper.renamePhotos(oldPrefix, newPrefix, memberId);
	}
	
	@Override
	public PhotoDetailResponseDto findPhotoDetailByPhotoIdAndMemberId(Long photoId, Long memberId) {
		return photoMapper.findPhotoDetailByPhotoIdAndMemberId(photoId, memberId);
	}

	@Override
	public List<DirectoryDto> findDirectoriesByPrefix(String prefix){
		return photoMapper.findDirectoriesByPrefix(prefix);
	}

	@Override
	public List<FileDto> findFilesByPrefix(String prefix){
		return photoMapper.findFilesByPrefix(prefix);
	}

	@Override
	public boolean existsByPrefix(String prefix){
		return photoMapper.existsByPrefix(prefix);
	}

	@Override
	public S3KeyOriginalFilenameDto findS3KeyAndOriginalFilenameByPhotoIdAndMemberId(Long photoId, Long memberId){
		return photoMapper.findS3KeyAndOriginalFilenameByPhotoIdAndMemberId(photoId, memberId);
	}

	@Override
	public List<S3KeyOriginalFilenameDto> findS3KeysAndOriginalFilenamesByPrefixAndMemberId(String prefix, Long memberId){
		return photoMapper.findS3KeysAndOriginalFilenamesByPrefixAndMemberId(prefix, memberId);
	}
}
