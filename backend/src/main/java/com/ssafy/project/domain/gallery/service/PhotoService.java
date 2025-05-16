package com.ssafy.project.domain.gallery.service;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.ThumbnailResponseDto;

public interface PhotoService {
    DirectoryResponseDto select(String prefix);
	void updateDescription(Long photoId, PhotoDescriptionRequestDto requestDto);
	void setThumbnail(Long photoId, String region);
	List<ThumbnailResponseDto> getThumbnails(String sido);
}
