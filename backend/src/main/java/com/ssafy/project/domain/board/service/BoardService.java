package com.ssafy.project.domain.board.service;

import com.ssafy.project.domain.board.dto.response.PostDetailResponseDto;
import org.springframework.web.multipart.MultipartFile;

import com.ssafy.project.common.response.OffsetPageResponse;
import com.ssafy.project.domain.board.dto.request.CommentRequestDto;
import com.ssafy.project.domain.board.dto.request.PostRequestDto;


public interface BoardService {
    void createPost(PostRequestDto postRequestDto);
    String uploadImage(MultipartFile image);
    void deletePost(Long postId);
    boolean togglePostLike(Long postId);
	void createComment(Long postId, CommentRequestDto commentRequestDto);
	OffsetPageResponse<?> getPagedPostList(Integer page, Integer size);
    PostDetailResponseDto getPost(Long postId);
    void deleteComment(Long postId, Long commentId);
    void updatePost(Long postId, PostRequestDto postRequestDto);
}
