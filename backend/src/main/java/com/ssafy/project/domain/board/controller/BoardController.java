package com.ssafy.project.domain.board.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.dto.request.PostRequestDto;
import com.ssafy.project.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // 게시글 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<?> like(@PathVariable Long postId) {
        return ResponseEntity.status(200)
                .body(createSuccess(boardService.togglePostLike(postId)));
    }

    // 게시글 작성
    @PostMapping
    private ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto) {
        boardService.createPost(postRequestDto);
        return ResponseEntity.status(201)
                .body(createSuccessWithNoContent());
    }

    // 게시글 첨부파일 업로드
    @PostMapping("/image")
    private ResponseEntity<?> uploadImage(@RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201)
                .body(createSuccess(boardService.uploadImage(image)));
    }
    
    // 댓글 작성
    @PostMapping("/{postId}/comments")
    private ResponseEntity<?> createComment(@PathVariable Long postId,
    		@RequestBody CommentRequestDto commentRequestDto){
    	boardService.createComment(postId, commentRequestDto);
    	return ResponseEntity.status(201)
    			.body(createSuccessWithNoContent());
    }
}
