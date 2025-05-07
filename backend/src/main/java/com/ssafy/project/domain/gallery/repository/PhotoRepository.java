package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;

public interface PhotoRepository {
	void insertPhotos(List<PhotoDto> photos);
}
