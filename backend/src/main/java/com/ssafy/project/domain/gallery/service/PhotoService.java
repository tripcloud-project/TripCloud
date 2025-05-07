package com.ssafy.project.domain.gallery.service;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.UploadDto;

public interface PhotoService {
	void uploadPhotos(List<UploadDto> uploadList);
}