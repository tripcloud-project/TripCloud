package com.ssafy.project.domain.gallery.service;

import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;

public interface PhotoService {
    DirectoryResponseDto select(String prefix);
	void updateDescription(Long photoId, PhotoDescriptionRequestDto requestDto);
	void setThumbnail(Long photoId, String region);
}
