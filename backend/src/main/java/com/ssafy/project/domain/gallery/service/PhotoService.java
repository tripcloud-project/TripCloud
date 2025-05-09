package com.ssafy.project.domain.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.request.RenameRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;

public interface PhotoService {
	void uploadPhotos(List<MultipartFile> files, String prefix);
	void renameObjects(RenameRequestDto renameRequestDto);
	PhotoDetailResponseDto getDetailPhoto(Long photoId);
	DirectoryResponseDto listDirectory(String prefix);
}