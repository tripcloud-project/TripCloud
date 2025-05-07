package com.ssafy.project.domain.gallery.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;
import com.ssafy.project.domain.gallery.mapper.PhotoMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PhotoRepositoryImpl implements PhotoRepository{
	private final PhotoMapper photoMapper;
	
	@Override
	public void insertPhotos(List<PhotoDto> photos) {
		photoMapper.insertPhotos(photos);
	}

}
