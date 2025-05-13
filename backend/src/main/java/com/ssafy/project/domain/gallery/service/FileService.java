package com.ssafy.project.domain.gallery.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.DownloadDto;
import com.ssafy.project.domain.gallery.dto.request.DeleteRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DirectoryRenameRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DownloadRequestDto;
import com.ssafy.project.domain.gallery.dto.request.RestoreRequestDto;
import com.ssafy.project.domain.gallery.dto.request.TrashRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.FileDetailResponseDto;
import com.ssafy.project.domain.gallery.dto.response.SearchResultResponseDto;

public interface FileService {
	void uploadFiles(List<MultipartFile> files, String prefix);
	void renameDirectory(DirectoryRenameRequestDto directoryRenameRequestDto);
	void renameFile(Long fileId, String filename);
	FileDetailResponseDto getDetailFile(Long fileId);
	DirectoryResponseDto listDirectory(String prefix, Boolean isDeleted);
	DownloadDto downloadBulk(DownloadRequestDto downloadRequestDto);
	void trashBulk(TrashRequestDto trashRequestDto);
	void restore(RestoreRequestDto restoreRequestDto);
	void delete(DeleteRequestDto deleteRequestDto);
	SearchResultResponseDto searchByKeyWord(String keyword);
	SearchResultResponseDto searchByHashtag(String hashtag);
}