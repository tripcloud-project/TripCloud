package com.ssafy.project.domain.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponse;

public interface MinioService {
	List<UploadDto> uploadFiles(List<MultipartFile> files, String prefix);
	DirectoryResponse listDirectory(String prefix);
	List<S3KeyUpdateDto> directoryKeyUpdate(String oldPrefix, String newPrefix);
	void keyUpdate(String oldKey, String newKey);
}