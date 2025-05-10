package com.ssafy.project.domain.gallery.service;

import java.util.List;

import com.ssafy.project.domain.gallery.dto.internal.DownloadDto;
import com.ssafy.project.domain.gallery.dto.request.DownloadRequestDto;
import com.ssafy.project.domain.gallery.dto.request.TrashRequestDto;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;

public interface PhotoService {
	void uploadPhotos(List<MultipartFile> files, String prefix);
	void renameDirectory(DirectoryRenameRequestDto directoryRenameRequestDto);
	void renamePhoto(Long photoId, String filename);
	PhotoDetailResponseDto getDetailPhoto(Long photoId);
	DirectoryResponseDto listDirectory(String prefix);
	DownloadDto downloadBulk(DownloadRequestDto downloadRequestDto);
	void trashBulk(TrashRequestDto trashRequestDto);
}