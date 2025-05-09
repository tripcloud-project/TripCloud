package com.ssafy.project.domain.gallery.mapper;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DirectoryDto;
import com.ssafy.project.domain.gallery.dto.internal.FileDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.ssafy.project.domain.gallery.dto.internal.PhotoDto;

@Mapper
public interface PhotoMapper {
	void insertPhoto(PhotoDto photo);
	void insertPhotos(List<PhotoDto> photos);
	void renamePhoto(@Param("oldKey") String oldKey, @Param("newKey") String newKey, @Param("originalFilename") String originalFilename);
	PhotoDetailResponseDto findPhotoDetailByS3Key(String key);
	List<DirectoryDto> findDirectoriesByPrefix(@Param("prefix") String prefix);
	List<FileDto> findFilesByPrefix(@Param("prefix") String prefix);
}
