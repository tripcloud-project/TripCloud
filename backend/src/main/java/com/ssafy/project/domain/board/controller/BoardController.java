package com.ssafy.project.domain.board.controller;

import static com.ssafy.project.common.response.ApiResponse.createSuccess;
import static com.ssafy.project.common.response.ApiResponse.createSuccessWithNoContent;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    private ResponseEntity<?> deletePost(@PathVariable("postId") Long postId) {
        boardService.deletePost(postId);
        return ResponseEntity.status(200)
                .body(createSuccessWithNoContent());
    }
    
    // 게시글 리스트 조회
    @GetMapping
    private ResponseEntity<?> getPagedPostList(@RequestParam Integer page, @RequestParam Integer size){
    	return ResponseEntity.status(200)
    			.body(createSuccess(boardService.getPagedPostList(page-1, size)));
    }

    // 게시글 상세 조회
    @GetMapping("/{postId}")
    private ResponseEntity<?> getPost(@PathVariable Long postId) {
        return ResponseEntity.status(200)
                .body(createSuccess(boardService.getPost(postId)));
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    private ResponseEntity<?> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        boardService.deleteComment(postId, commentId);
        return ResponseEntity.status(200)
                .body(createSuccessWithNoContent());
    }

    // 게시글 수정
    @PutMapping("/{postId}")
    private ResponseEntity<?> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto) {
        boardService.updatePost(postId, postRequestDto);
        return ResponseEntity.status(200)
                .body(createSuccessWithNoContent());
    }
}
