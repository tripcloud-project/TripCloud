package com.ssafy.project.domain.gallery.service;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;

public interface PhotoService {
	void uploadPhotos(List<UploadDto> uploadList);
	void renamePhotos(List<S3KeyUpdateDto> renameList);
	void renamePhoto(String oldKey, String newKey);
}