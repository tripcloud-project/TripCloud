package com.ssafy.project.domain.gallery.controller;

import com.ssafy.project.domain.gallery.service.PhotoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static com.ssafy.project.common.response.ApiResponse.createSuccess;

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

}
