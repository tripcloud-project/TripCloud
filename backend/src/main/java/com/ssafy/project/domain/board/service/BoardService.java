package com.ssafy.project.domain.board.service;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import org.springframework.web.multipart.MultipartFile;


public interface BoardService {
    void createPost(PostRequestDto postRequestDto);
    String uploadImage(MultipartFile image);
    void deletePost(Long postId);
    boolean togglePostLike(Long postId);
}
