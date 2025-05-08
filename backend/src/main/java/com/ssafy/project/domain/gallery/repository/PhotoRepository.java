package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;

public interface PhotoRepository {
	void insertPhotos(List<PhotoDto> photos);
	void renamePhoto(String oldKey, String newKey, String filename);
    PhotoDetailResponseDto findPhotoDetailByS3Key(String key);
}
