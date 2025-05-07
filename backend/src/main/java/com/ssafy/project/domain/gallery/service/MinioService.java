package com.ssafy.project.domain.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.UploadDto;

public interface MinioService {
	List<UploadDto> uploadFiles(List<MultipartFile> files, String prefix);
}