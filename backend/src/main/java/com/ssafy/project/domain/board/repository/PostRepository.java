package com.ssafy.project.domain.board.repository;

import com.ssafy.project.domain.board.dto.PostRequestDto;
import lombok.NonNull;

public interface PostRepository {
    void insert(PostRequestDto postRequestDto);
    boolean delete(@NonNull Long memberId, Long postId);
}
