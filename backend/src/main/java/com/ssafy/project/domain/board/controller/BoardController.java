package com.ssafy.project.domain.board.controller;

import com.ssafy.project.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 첨부파일 업로드
    @PostMapping("/image")
    private ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201)
                .body(createSuccess(boardService.uploadImage(image)));
    }
}
