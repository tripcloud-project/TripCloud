package com.ssafy.project.domain.gallery.controller;

import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.dto.request.RenameRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.service.MinioService;
import com.ssafy.project.domain.gallery.service.PhotoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gallery")
@Slf4j
public class PhotoController {
    private final MinioService minioService;
    private final PhotoService photoService;

    @Value("${minio.bucket}")
    private String buckName;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") List<MultipartFile> files,
                                    @RequestParam("prefix") String prefix) {
        if (files == null || files.isEmpty()) {
            throw new UploadFailException();
        }
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        prefix = String.format("%s/%s", email, prefix.replaceAll("^/+", ""));

        try {
            List<UploadDto> uploadList = minioService.uploadFiles(files, prefix);
            photoService.uploadPhotos(uploadList);
            return ResponseEntity.ok(ApiResponse.createSuccessWithNoContent());
        } catch (Exception e) {
            throw new UploadFailException();
        }
    }
    
    @GetMapping("/list")
	public ResponseEntity<?> list(@RequestParam String prefix) {
    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
        prefix = String.format("%s/%s", email, prefix.replaceAll("^/+", ""));
		DirectoryResponseDto directoryResponseDto = minioService.listDirectory(prefix);
	    return ResponseEntity.ok(ApiResponse.createSuccess(directoryResponseDto));
	}
    
    
	@PutMapping("/rename")
	public ResponseEntity<?> rename(@RequestBody RenameRequestDto renameRequestDto) {
    	String email = SecurityContextHolder.getContext().getAuthentication().getName();
		String oldKey = renameRequestDto.getOldKey();
		oldKey = String.format("%s/%s", email, oldKey.replaceAll("^/+", ""));
		String newKey = renameRequestDto.getNewKey();
		newKey = String.format("%s/%s", email, newKey.replaceAll("^/+", ""));
		try {
			if (oldKey.endsWith("/")) {
				// 디렉토리 이름 번경
				List<S3KeyUpdateDto> renameList = minioService.directoryKeyUpdate(oldKey, newKey);
				photoService.renamePhotos(renameList);
			} else {
				// 파일 이름 변경
				minioService.keyUpdate(oldKey, newKey);
				photoService.renamePhoto(oldKey, newKey);
			}

			return ResponseEntity.ok(ApiResponse.createSuccessWithNoContent());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("이름 변경 실패: " + e.getMessage());
		}
	}


	@GetMapping("/detail")
	public ResponseEntity<?> viewMeta(@RequestParam String key) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		key = String.format("%s/%s", email, key.replaceAll("^/+", ""));
		try {
			PhotoDetailResponseDto photoDetailResponse = photoService.getDetailPhoto(key);
			return ResponseEntity.ok(ApiResponse.createSuccess(photoDetailResponse));
		} catch (NoSuchKeyException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(Map.of("error", "파일이 존재하지 않습니다", "key", key));
		}
	}
}
