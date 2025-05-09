package com.ssafy.project.domain.gallery.controller;

import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.gallery.dto.internal.S3KeyUpdateDto;
import com.ssafy.project.domain.gallery.dto.internal.UploadDto;
import com.ssafy.project.domain.gallery.dto.request.RenameRequestDto;
import com.ssafy.project.domain.gallery.dto.response.DirectoryResponseDto;
import com.ssafy.project.domain.gallery.dto.response.PhotoDetailResponseDto;
import com.ssafy.project.domain.gallery.exception.UploadFailException;
import com.ssafy.project.domain.gallery.service.PhotoService;
import com.ssafy.project.infra.s3.S3Service;
import com.ssafy.project.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    private final PhotoService photoService;

    @Value("${minio.bucket}")
    private String buckName;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") List<MultipartFile> files,
                                    @RequestParam("prefix") String prefix) {
        photoService.uploadPhotos(files, prefix);
        return ResponseEntity.status(201)
        		.body(ApiResponse.createSuccessWithNoContent());
    }
    
    @GetMapping("/list")
	public ResponseEntity<?> list(@RequestParam String prefix) {
	    return ResponseEntity.status(200)
	    		.body(ApiResponse.createSuccess(photoService.listDirectory(prefix)));
	}
    
    
	@PutMapping("/rename")
	public ResponseEntity<?> rename(@RequestBody RenameRequestDto renameRequestDto) {
		photoService.renameObjects(renameRequestDto);
		return ResponseEntity.status(200)
				.body(ApiResponse.createSuccessWithNoContent());
	}


	@GetMapping("/detail/{photoId}")
	public ResponseEntity<?> viewMeta(@PathVariable Long photoId) {
		return ResponseEntity.status(200)
				.body(ApiResponse.createSuccess(photoService.getDetailPhoto(photoId)));
	}
}
