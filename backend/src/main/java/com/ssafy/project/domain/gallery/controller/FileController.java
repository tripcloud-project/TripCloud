package com.ssafy.project.domain.gallery.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.gallery.dto.internal.DownloadDto;
import com.ssafy.project.domain.gallery.dto.request.DeleteRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DirectoryRenameRequestDto;
import com.ssafy.project.domain.gallery.dto.request.DownloadRequestDto;
import com.ssafy.project.domain.gallery.dto.request.FileRenameRequestDto;
import com.ssafy.project.domain.gallery.dto.request.RestoreRequestDto;
import com.ssafy.project.domain.gallery.dto.request.TrashRequestDto;
import com.ssafy.project.domain.gallery.dto.response.SearchResultResponseDto;
import com.ssafy.project.domain.gallery.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gallery")
@Slf4j
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("files") List<MultipartFile> files,
                                    @RequestParam("prefix") String prefix) {
        fileService.uploadFiles(files, prefix);
        return ResponseEntity.status(201)
        		.body(createSuccessWithNoContent());
    }
    
    @GetMapping("/list")
	public ResponseEntity<?> list(@RequestParam String prefix) {
	    return ResponseEntity.status(200)
	    		.body(createSuccess(fileService.listDirectory(prefix, false)));
	}

	@PutMapping("/rename/{fileId}")
	public ResponseEntity<?> renameFile(@PathVariable Long fileId, @RequestBody FileRenameRequestDto fileRenameRequestDto) {
		fileService.renameFile(fileId, fileRenameRequestDto.getFilename());
		return ResponseEntity.status(200)
				.body(createSuccessWithNoContent());
	}

	@PutMapping("/rename")
	public ResponseEntity<?> renameDirectory(@RequestBody DirectoryRenameRequestDto directoryRenameRequestDto) {
		fileService.renameDirectory(directoryRenameRequestDto);
		return ResponseEntity.status(200)
				.body(createSuccessWithNoContent());
	}

	@GetMapping("/detail/{fileId}")
	public ResponseEntity<?> viewMeta(@PathVariable Long fileId) {
		return ResponseEntity.status(200)
				.body(createSuccess(fileService.getDetailFile(fileId)));
	}

	@PostMapping("/download")
	public ResponseEntity<?> download(@RequestBody DownloadRequestDto downloadRequestDto) {
		DownloadDto downloadDto = fileService.downloadBulk(downloadRequestDto);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, downloadDto.getContentDisposition())
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(downloadDto.getResource());
	}

	@DeleteMapping("/trash")
	public ResponseEntity<?> trash(@RequestBody TrashRequestDto trashRequestDto) {
		fileService.trashBulk(trashRequestDto);
		return ResponseEntity.status(200)
				.body(createSuccessWithNoContent());
	}

	@GetMapping("/trash/list")
	public ResponseEntity<?> trashList() {
		return ResponseEntity.status(200)
				.body(createSuccess(fileService.listDirectory("/", true)));
	}

	@PutMapping("/trash/restore")
	public ResponseEntity<?> restore(@RequestBody RestoreRequestDto restoreRequestDto) {
		fileService.restore(restoreRequestDto);
		return ResponseEntity.status(200)
				.body(createSuccessWithNoContent());
	}

	@DeleteMapping("/trash/delete")
	public ResponseEntity<?> delete(@RequestBody DeleteRequestDto deleteRequestDto) {
		fileService.delete(deleteRequestDto);
		return ResponseEntity.status(200)
				.body(createSuccessWithNoContent());
	}
	
    @GetMapping
    public ResponseEntity<?> getDirectoryStructure(){
    	return ResponseEntity.status(200)
    			.body(createSuccess(fileService.getDirectoryStructure()));
    }
    
	// TODO: 파일 이름 검색 endpoint 추가

	// TODO: 해시태그 검색 endpoint 추가

	// 검색 API
	@GetMapping("/search")
	public ResponseEntity<?> search(@RequestParam(required = false) String keyword, 
			@RequestParam(required = false) String hashtag, 
			@RequestParam(required = false) String description){
		SearchResultResponseDto searchResult = null;
		
		if(keyword != null)
			searchResult = fileService.searchByKeyWord(keyword);
		else if(hashtag != null)
			searchResult = fileService.searchByHashtag(hashtag);
		else if(description != null)
			searchResult = fileService.searchByDescription(description);
		
		return ResponseEntity.status(200)
				.body(createSuccess(searchResult));
	}
}
