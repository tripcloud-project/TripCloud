package com.ssafy.project.domain.gallery.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;

@Mapper
public interface PhotoMapper {
	void insertPhoto(PhotoDto photo);
	void insertPhotos(List<PhotoDto> photos);

}
