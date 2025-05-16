package com.ssafy.project.domain.gallery.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.project.domain.gallery.dto.request.PhotoDescriptionRequestDto;
import com.ssafy.project.domain.gallery.service.PhotoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/gallery")
@Slf4j
public class PhotoController {
    private final PhotoService photoService;

    @GetMapping("/photo")
    public ResponseEntity<?> select(@RequestParam("prefix") String prefix){
        return ResponseEntity.status(200)
                .body(createSuccess(photoService.select(prefix)));
    }
    
    // 사진 설명 수정
    @PutMapping("/photo/{photoId}/description")
    public ResponseEntity<?> updateDescription(@PathVariable("photoId") Long photoId,
    		@RequestBody PhotoDescriptionRequestDto requestDto){
    	photoService.updateDescription(photoId, requestDto);
    	return ResponseEntity.status(200)
    			.body(createSuccessWithNoContent());
    }

    // 대표 이미지 설정
    @PutMapping("/photo/{photoId}/thumbnail")
    public ResponseEntity<?> setThumbnail(@PathVariable("photoId") Long photoId,
    		@RequestParam("region") String region){
    	photoService.setThumbnail(photoId, region);
    	return ResponseEntity.status(200)
    			.body(createSuccessWithNoContent());
    }
}
