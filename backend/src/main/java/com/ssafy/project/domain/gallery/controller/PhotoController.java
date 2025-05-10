package com.ssafy.project.domain.gallery.controller;

import com.ssafy.project.common.response.ApiResponse;
import com.ssafy.project.domain.gallery.dto.internal.DownloadDto;
import com.ssafy.project.domain.gallery.dto.request.DownloadRequestDto;
import com.ssafy.project.domain.gallery.dto.request.RenameRequestDto;
import com.ssafy.project.domain.gallery.dto.request.TrashRequestDto;
import com.ssafy.project.domain.gallery.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

	// TODO: 사진/디렉토리 endpoint 분리 필요.
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

	@PostMapping("/download")
	public ResponseEntity<?> download(@RequestBody DownloadRequestDto downloadRequestDto) {
		DownloadDto downloadDto = photoService.downloadBulk(downloadRequestDto);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(downloadDto.getResource());
	}

	@DeleteMapping("/trash")
	public ResponseEntity<?> trash(@RequestBody TrashRequestDto trashRequestDto) {
		photoService.trashBulk(trashRequestDto);
		return ResponseEntity.status(200)
				.body(ApiResponse.createSuccessWithNoContent());
	}

	// TODO: 휴지통 조회 endpoint 추가

	// TODO: 파일 복원 endpoint 추가

	// TODO: 영구삭제 endpoint 추가

	// TODO: 휴지통 endpoint 추가

	// TODO: 파일 이름 검색 endpoint 추가

	// TODO: 해시태그 검색 endpoint 추가
}
