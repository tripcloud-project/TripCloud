package com.ssafy.project.domain.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;

public interface PhotoService {
	void uploadPhotos(List<MultipartFile> files, String prefix);
	void renamePhotos(List<S3KeyUpdateDto> renameList);
	void renamePhoto(String oldKey, String newKey);
	PhotoDetailResponseDto getDetailPhoto(String key);
	DirectoryResponseDto listDirectory(String prefix);
}