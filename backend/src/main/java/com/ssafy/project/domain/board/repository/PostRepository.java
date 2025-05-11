package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.PostRequestDto;

public interface PostRepository {
    void insert(PostRequestDto postRequestDto);
}
